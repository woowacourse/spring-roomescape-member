const { test, expect } = require("@playwright/test");

const ADMIN_PATH = "/admin";
const RESERVE_PATH = "/reserve";

test.describe.configure({ mode: "serial" });

test("사용자 예약 후 관리자 예약 페이지에서 예약 내역을 확인할 수 있다", async ({ page, request }) => {
  const suffix = uniqueSuffix();
  const reservationName = `user-${suffix}`;
  const reservationDate = futureDate(20);

  const themes = await fetchThemes(request);
  const selectedTheme = themes[0];
  const selectedSlot = await findFirstReservableSlot(request, selectedTheme.id, reservationDate);

  await page.goto(RESERVE_PATH);
  await page.selectOption("#themeSelect", String(selectedTheme.id));
  await page.fill("#nameInput", reservationName);
  await page.fill("#dateInput", reservationDate);
  await page.getByRole("button", { name: selectedSlot.startAt.slice(0, 5), exact: true }).click();
  await page.getByRole("button", { name: "예약 확정" }).click();

  await expect(page).toHaveURL(/\/$/);

  await page.goto(ADMIN_PATH);
  await page.getByRole("button", { name: "예약" }).click();

  const reservationRow = page.locator('[data-role="reservation-table"] tr').filter({ hasText: reservationName });
  await expect(reservationRow).toContainText(reservationDate);
  await expect(reservationRow).toContainText(selectedSlot.startAt.slice(0, 5));
});

test("관리자가 테마를 추가하면 사용자 테마 목록에서 조회된다", async ({ page }) => {
  const suffix = uniqueSuffix();
  const themeName = `theme-${suffix}`;
  const description = `theme-description-${suffix}`;
  const thumbnailImageUrl = `https://example.com/theme-${suffix}.png`;

  await page.goto(ADMIN_PATH);
  await createTheme(page, { themeName, description, thumbnailImageUrl });

  const adminThemeRow = page.locator('[data-role="theme-table"] tr').filter({ hasText: themeName });
  await expect(adminThemeRow).toContainText(description);

  await page.goto(RESERVE_PATH);
  await expect(page.locator("#themeSelect option").filter({ hasText: themeName })).toHaveCount(1);
});

test("관리자가 테마를 삭제하면 사용자 테마 목록에서 사라진다", async ({ page }) => {
  const suffix = uniqueSuffix();
  const themeName = `delete-${suffix}`;

  await page.goto(ADMIN_PATH);
  await createTheme(page, {
    themeName,
    description: `delete-description-${suffix}`,
    thumbnailImageUrl: `https://example.com/delete-${suffix}.png`
  });

  await expect(page.locator('[data-role="theme-table"] tr').filter({ hasText: themeName })).toHaveCount(1);
  await deleteTheme(page, themeName);
  await expect(page.locator('[data-role="theme-table"] tr').filter({ hasText: themeName })).toHaveCount(0);

  await page.goto(RESERVE_PATH);
  await expect(page.locator("#themeSelect option").filter({ hasText: themeName })).toHaveCount(0);
});

test("예약된 시간은 비활성화되고 관리자 취소 후 다시 예약 가능해진다", async ({ page, request }) => {
  const suffix = uniqueSuffix();
  const reservationName = `reopen-${suffix}`;
  const reservationDate = futureDate(21);

  const themes = await fetchThemes(request);
  const selectedTheme = themes[0];
  const selectedSlot = await findFirstReservableSlot(request, selectedTheme.id, reservationDate);
  const slotLabel = selectedSlot.startAt.slice(0, 5);

  await page.goto(RESERVE_PATH);
  await page.selectOption("#themeSelect", String(selectedTheme.id));
  await page.fill("#nameInput", reservationName);
  await page.fill("#dateInput", reservationDate);
  await page.getByRole("button", { name: slotLabel, exact: true }).click();
  await page.getByRole("button", { name: "예약 확정" }).click();
  await expect(page).toHaveURL(/\/$/);

  await page.goto(RESERVE_PATH);
  await page.selectOption("#themeSelect", String(selectedTheme.id));
  await page.fill("#dateInput", reservationDate);
  const disabledSlot = page.locator(".slot-btn.disabled", { hasText: slotLabel });
  await expect(disabledSlot).toHaveCount(1);

  await page.goto(ADMIN_PATH);
  await page.getByRole("button", { name: "예약" }).click();
  await cancelReservation(page, reservationName);
  await expect(page.locator('[data-role="reservation-table"] tr').filter({ hasText: reservationName })).toHaveCount(0);

  await page.goto(RESERVE_PATH);
  await page.selectOption("#themeSelect", String(selectedTheme.id));
  await page.fill("#dateInput", reservationDate);
  await expect(page.locator(".slot-btn.disabled", { hasText: slotLabel })).toHaveCount(0);
  await expect(page.getByRole("button", { name: slotLabel, exact: true })).toBeEnabled();
});

async function fetchThemes(request) {
  const response = await request.get("/api/themes");
  expect(response.ok()).toBeTruthy();
  return response.json();
}

async function findFirstReservableSlot(request, themeId, date) {
  const response = await request.get(`/api/themes/${themeId}?date=${date}`);
  expect(response.ok()).toBeTruthy();

  const slots = await response.json();
  const reservableSlot = slots.find((slot) => slot.isReservable);

  expect(reservableSlot, `No reservable slot found for theme ${themeId} on ${date}`).toBeTruthy();
  return reservableSlot;
}

async function createTheme(page, { themeName, description, thumbnailImageUrl }) {
  await page.fill("#themeName", themeName);
  await page.fill("#themeDescription", description);
  await page.fill("#themeThumbnail", thumbnailImageUrl);
  await page.getByRole("button", { name: "추가하기" }).first().click();
  await expect(page.locator('[data-role="toast"]')).toContainText("테마가 추가되었습니다.");
}

async function deleteTheme(page, themeName) {
  const row = page.locator('[data-role="theme-table"] tr').filter({ hasText: themeName });
  await row.getByRole("button", { name: "삭제" }).click();
  await expect(page.locator('[data-role="toast"]')).toContainText("테마가 삭제되었습니다.");
}

async function cancelReservation(page, reservationName) {
  const row = page.locator('[data-role="reservation-table"] tr').filter({ hasText: reservationName });
  await row.getByRole("button", { name: "취소" }).click();
  await expect(page.locator('[data-role="toast"]')).toContainText("예약이 취소되었습니다.");
}

function futureDate(daysToAdd) {
  const date = new Date();
  date.setDate(date.getDate() + daysToAdd);
  return date.toISOString().split("T")[0];
}

function uniqueSuffix() {
  return String(Date.now()).slice(-6);
}
