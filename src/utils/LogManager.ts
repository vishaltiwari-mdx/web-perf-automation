import * as fs   from 'fs';
import * as path from 'path';

// ── Types ──────────────────────────────────────────────────────────────────────

export type LogLevel = 'INFO' | 'WARN' | 'ERROR' | 'DEBUG';

export interface LogEntry {
  ts:      string;   // ISO timestamp
  level:   LogLevel;
  message: string;
}

// ── Module state ───────────────────────────────────────────────────────────────

let logPath  = '';
let entries: LogEntry[] = [];
let active   = false;

// Snapshot of whatever console methods existed at the moment init() was called.
// Captured per-init so we correctly chain on top of any hooks already installed
// (e.g. the server.ts SSE emitter that replaces console before calling analyze()).
let _prevInfo:  (...a: unknown[]) => void = console.info.bind(console);
let _prevLog:   (...a: unknown[]) => void = console.log.bind(console);
let _prevWarn:  (...a: unknown[]) => void = console.warn.bind(console);
let _prevError: (...a: unknown[]) => void = console.error.bind(console);
let _prevDebug: (...a: unknown[]) => void = console.debug.bind(console);

// ── Public API ─────────────────────────────────────────────────────────────────

/**
 * Start log capture.
 * Chains onto whatever console methods are installed at call-time — including
 * any SSE emitter installed by server.ts — so all output paths remain active.
 * Writes entries to `<reportDir>/run-<timestamp>.log`.
 *
 * Safe to call multiple times — re-init clears the previous buffer.
 */
export function init(reportDir = 'reports'): void {
  entries = [];
  active  = true;
  fs.mkdirSync(reportDir, { recursive: true });
  logPath = path.join(reportDir, `run-${tsFileSafe()}.log`);

  // Write header immediately so the file exists as soon as the run starts
  const header = [
    '═'.repeat(72),
    `  Web Performance Analyzer — Run Log`,
    `  Started : ${new Date().toISOString()}`,
    `  File    : ${logPath}`,
    '═'.repeat(72),
    '',
  ].join('\n');
  fs.writeFileSync(logPath, header, 'utf8');

  installHooks();
  // Use _prevInfo (not console.info) to avoid infinite loop since hooks are now active
  _prevInfo(`[LogManager] Logging to: ${logPath}`);
}

/**
 * Flush all buffered entries to disk and restore the console methods that were
 * active before init() was called.  Returns the path of the written log file.
 */
export function flush(): string {
  if (!logPath) return '';

  const footer = [
    '',
    '═'.repeat(72),
    `  Finished : ${new Date().toISOString()}`,
    `  Entries  : ${entries.length}`,
    '═'.repeat(72),
  ].join('\n');

  fs.appendFileSync(logPath, footer, 'utf8');
  active = false;
  restoreHooks();
  _prevInfo(`[LogManager] Log saved: ${logPath}`);
  return logPath;
}

/** Returns all captured log entries (used by HtmlReportManager to embed the log). */
export function getEntries(): LogEntry[] {
  return entries.slice();
}

/** Returns the path of the current log file (empty string before init). */
export function getLogPath(): string {
  return logPath;
}

/**
 * Write a log entry directly without going through console.
 * Useful for structured lines that should not appear in terminal output.
 */
export function write(level: LogLevel, message: string): void {
  if (!active) return;
  appendEntry(level, message);
}

// ── Console hook installation ──────────────────────────────────────────────────

function installHooks(): void {
  // Snapshot the current console methods so we can chain on top of them
  // (captures SSE emitters, prior LogManager installs, or any other hooks).
  _prevInfo  = console.info.bind(console);
  _prevLog   = console.log.bind(console);
  _prevWarn  = console.warn.bind(console);
  _prevError = console.error.bind(console);
  _prevDebug = console.debug.bind(console);

  console.info = (...args: unknown[]) => {
    _prevInfo(...args);
    appendEntry('INFO', formatArgs(args));
  };
  console.log = (...args: unknown[]) => {
    _prevLog(...args);
    appendEntry('INFO', formatArgs(args));
  };
  console.warn = (...args: unknown[]) => {
    _prevWarn(...args);
    appendEntry('WARN', formatArgs(args));
  };
  console.error = (...args: unknown[]) => {
    _prevError(...args);
    appendEntry('ERROR', formatArgs(args));
  };
  console.debug = (...args: unknown[]) => {
    _prevDebug(...args);
    appendEntry('DEBUG', formatArgs(args));
  };
}

function restoreHooks(): void {
  console.info  = _prevInfo;
  console.log   = _prevLog;
  console.warn  = _prevWarn;
  console.error = _prevError;
  console.debug = _prevDebug;
}

// ── Internal helpers ───────────────────────────────────────────────────────────

function appendEntry(level: LogLevel, message: string): void {
  if (!active || !logPath) return;
  const ts    = new Date().toISOString();
  const entry: LogEntry = { ts, level, message };
  entries.push(entry);

  const line = `[${ts}] [${level.padEnd(5)}] ${message}\n`;
  try {
    fs.appendFileSync(logPath, line, 'utf8');
  } catch { /* ignore write errors — don't break the test run */ }
}

function formatArgs(args: unknown[]): string {
  return args
    .map(a => {
      if (typeof a === 'string') return a;
      if (a instanceof Error)    return `${a.message}\n${a.stack ?? ''}`;
      try { return JSON.stringify(a); } catch { return String(a); }
    })
    .join(' ');
}

function tsFileSafe(): string {
  return new Date().toISOString().replace(/[:.]/g, '-').replace('T', '_').slice(0, 23);
}
