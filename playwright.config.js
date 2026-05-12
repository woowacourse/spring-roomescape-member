const { defineConfig, devices } = require("@playwright/test");

const webServer = process.env.PW_SKIP_WEBSERVER
  ? undefined
  : {
      command: "GRADLE_USER_HOME=.gradle-playwright ./gradlew bootRun",
      url: "http://127.0.0.1:8080",
      reuseExistingServer: true,
      timeout: 120000
    };

module.exports = defineConfig({
  testDir: "./e2e",
  fullyParallel: false,
  retries: 0,
  reporter: [["html", { open: "never" }]],
  use: {
    baseURL: "http://127.0.0.1:8080",
    trace: "on-first-retry",
    screenshot: "only-on-failure",
    video: "retain-on-failure"
  },
  webServer,
  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] }
    }
  ]
});
