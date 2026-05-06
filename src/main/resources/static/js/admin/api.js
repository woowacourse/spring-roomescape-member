import { requestJson } from "../common/http.js";

const ADMIN_HEADERS = {
  "Content-Type": "application/json",
  role: "ADMIN"
};

const ADMIN_ONLY_HEADERS = {
  role: "ADMIN"
};

export function fetchThemes() {
  return requestJson("/api/themes");
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
  return requestJson("/api/admin/reservations", {
    headers: ADMIN_ONLY_HEADERS
  });
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
  return requestJson("/api/admin/times", {
    headers: ADMIN_ONLY_HEADERS
  });
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
