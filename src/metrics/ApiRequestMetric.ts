/**
 * ApiRequestMetric — DTO for a slow XHR / fetch request (duration >= threshold).
 */
export interface ApiRequestMetric {
  url: string;
  durationMs: number;
  type: string;
}
