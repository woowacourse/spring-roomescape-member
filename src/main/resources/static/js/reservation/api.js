import { requestJson } from "../common/http.js";

const DEFAULT_PAGE = 0;
const DEFAULT_SIZE = 100;

function unwrapResponses(response) {
  return response.responses;
}

export function fetchThemes() {
  const searchParams = new URLSearchParams({
    page: String(DEFAULT_PAGE),
    size: String(DEFAULT_SIZE)
  });

  return requestJson(`/api/themes?${searchParams.toString()}`).then(unwrapResponses);
}

export function fetchThemeSlots(themeId, date) {
  const searchParams = new URLSearchParams({ date });

  return requestJson(`/api/themes/${themeId}/times?${searchParams.toString()}`).then(unwrapResponses);
}

export function createReservation(payload) {
  return requestJson("/api/reservations", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
}
