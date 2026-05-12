import { requestJson } from "../common/http.js";

const DEFAULT_PAGE = 0;
const DEFAULT_SIZE = 100;
const POPULAR_THEME_LIMIT = 10;

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

export function fetchPopularThemes(startDate, endDate) {
  const searchParams = new URLSearchParams({
    startDate,
    endDate,
    limit: String(POPULAR_THEME_LIMIT)
  });

  return requestJson(`/api/themes/popular?${searchParams.toString()}`).then(unwrapResponses);
}
