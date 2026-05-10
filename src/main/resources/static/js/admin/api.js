import { requestJson } from "../common/http.js";

const ADMIN_HEADERS = {
  "Content-Type": "application/json",
  role: "ADMIN"
};

const ADMIN_ONLY_HEADERS = {
  role: "ADMIN"
};

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

export function createTheme(payload) {
  return requestJson("/api/admin/themes", {
    method: "POST",
    headers: ADMIN_HEADERS,
    body: JSON.stringify(payload)
  });
}

export function deleteTheme(id) {
  return requestJson(`/api/admin/themes/${id}`, {
    method: "DELETE",
    headers: ADMIN_ONLY_HEADERS
  });
}

export function fetchReservations() {
  const searchParams = new URLSearchParams({
    page: String(DEFAULT_PAGE),
    size: String(DEFAULT_SIZE)
  });

  return requestJson(`/api/admin/reservations?${searchParams.toString()}`, {
    headers: ADMIN_ONLY_HEADERS
  }).then(unwrapResponses);
}

export function createReservation(payload) {
  return requestJson("/api/admin/reservations", {
    method: "POST",
    headers: ADMIN_HEADERS,
    body: JSON.stringify(payload)
  });
}

export function deleteReservation(id) {
  return requestJson(`/api/admin/reservations/${id}`, {
    method: "DELETE",
    headers: ADMIN_ONLY_HEADERS
  });
}

export function fetchTimes() {
  const searchParams = new URLSearchParams({
    page: String(DEFAULT_PAGE),
    size: String(DEFAULT_SIZE)
  });

  return requestJson(`/api/admin/times?${searchParams.toString()}`, {
    headers: ADMIN_ONLY_HEADERS
  }).then(unwrapResponses);
}

export function createTime(payload) {
  return requestJson("/api/admin/times", {
    method: "POST",
    headers: ADMIN_HEADERS,
    body: JSON.stringify(payload)
  });
}

export function deleteTime(id) {
  return requestJson(`/api/admin/times/${id}`, {
    method: "DELETE",
    headers: ADMIN_ONLY_HEADERS
  });
}
