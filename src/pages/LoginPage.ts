import { Page } from '@playwright/test';

/**
 * LoginPage — page object for the Helix IPLM sign-in screen.
 *
 * Selectors match CLAUDE.md / phi_web_automation source.
 * Generic CSS fallbacks are tried when the Helix-specific XPath is absent
 * so that the same class can be reused against other apps.
 */
export class LoginPage {
  private readonly page: Page;
  private readonly timeout: number;

  // Helix IPLM specific (from CLAUDE.md)
  private static readonly USERNAME = "xpath=//input[contains(@class,'signin-page__card__username-input')]";
  private static readonly PASSWORD = "xpath=//input[contains(@class,'signin-page__card__password-input')]";
  private static readonly SUBMIT   = "xpath=//button[contains(@class,'signin-page__card__submit-button')]";

  // Generic fallbacks for non-Helix apps
  private static readonly USERNAME_FB = "input[type='text'],input[type='email'],input[name='username'],#username,#email";
  private static readonly PASSWORD_FB = "input[type='password'],input[name='password'],#password";
  private static readonly SUBMIT_FB   = "button[type='submit'],input[type='submit'],.login-btn,#login-button";

  constructor(page: Page, timeoutMs = 30_000) {
    this.page    = page;
    this.timeout = timeoutMs;
  }

  async login(username: string, password: string): Promise<void> {
    const usernameInput = this.page.locator(LoginPage.USERNAME)
      .or(this.page.locator(LoginPage.USERNAME_FB)).first();
    const passwordInput = this.page.locator(LoginPage.PASSWORD)
      .or(this.page.locator(LoginPage.PASSWORD_FB)).first();
    const submitBtn     = this.page.locator(LoginPage.SUBMIT)
      .or(this.page.locator(LoginPage.SUBMIT_FB)).first();

    await usernameInput.waitFor({ state: 'visible', timeout: this.timeout });
    await usernameInput.fill(username);

    await passwordInput.waitFor({ state: 'visible', timeout: this.timeout });
    await passwordInput.fill(password);

    await submitBtn.waitFor({ state: 'visible', timeout: this.timeout });
    await submitBtn.click();
  }
}
