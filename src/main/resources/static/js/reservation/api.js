import { requestJson } from "../common/http.js";

export function fetchThemes() {
  return requestJson("/api/themes");
}

export function fetchThemeSlots(themeId, date) {
  return requestJson(`/api/themes/${themeId}?date=${date}`);
}

export function createReservation(payload) {
  return requestJson("/api/reservations", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
}
