const state = {
  themes: [],
  rankingThemes: [],
  selectedThemeId: null,
  selectedDate: formatDate(new Date()),
  selectedTimeId: null,
  availableTimes: [],
};

const elements = {
  themeList: document.querySelector("#theme-list"),
  rankingList: document.querySelector("#ranking-list"),
  rankingPeriod: document.querySelector("#ranking-period"),
  selectedTheme: document.querySelector("#selected-theme"),
  dateInput: document.querySelector("#reservation-date"),
  timeList: document.querySelector("#time-list"),
  form: document.querySelector("#reservation-form"),
  nameInput: document.querySelector("#reservation-name"),
  submitButton: document.querySelector("#submit-button"),
  result: document.querySelector("#reservation-result"),
  searchForm: document.querySelector("#reservation-search-form"),
  searchNameInput: document.querySelector("#reservation-search-name"),
  searchResult: document.querySelector("#reservation-search-result"),
  toast: document.querySelector("#toast"),
};

document.addEventListener("DOMContentLoaded", initialize);

async function initialize() {
  elements.dateInput.value = state.selectedDate;
  bindEvents();
  await loadThemes();
  await loadRankingThemes();
  await loadAvailableTimes();
}

function bindEvents() {
  elements.dateInput.addEventListener("change", handleDateChange);
  elements.form.addEventListener("submit", handleReservationSubmit);
  elements.searchForm.addEventListener("submit", handleReservationSearch);
}

async function loadThemes() {
  renderLoading(elements.themeList, "테마를 불러오는 중입니다.");
  state.themes = await requestJson("/themes");
  state.selectedThemeId = firstThemeId();
  renderThemes();
}

async function loadRankingThemes() {
  const period = recentWeekPeriod();
  elements.rankingPeriod.textContent = `${period.startDate} - ${period.endDate}`;
  renderLoading(elements.rankingList, "인기 테마를 불러오는 중입니다.");
  state.rankingThemes = await requestJson(rankingUrl(period));
  renderRankingThemes();
}

async function loadAvailableTimes() {
  resetSelectedTime();
  renderLoading(elements.timeList, "시간을 불러오는 중입니다.");
  updateSubmitButton();
  state.availableTimes = await requestJson(availableTimeUrl());
  renderAvailableTimes();
  updateSubmitButton();
}

function firstThemeId() {
  const firstTheme = state.themes.at(0);
  if (firstTheme) {
    return firstTheme.id;
  }
  return null;
}

function renderThemes() {
  renderThemeCards(elements.themeList, state.themes, handleThemeSelect);
  renderSelectedTheme();
}

function renderRankingThemes() {
  renderThemeCards(elements.rankingList, state.rankingThemes, handleThemeSelect);
}

function renderSelectedTheme() {
  const theme = selectedTheme();
  if (theme) {
    elements.selectedTheme.textContent = theme.name;
    return;
  }
  elements.selectedTheme.textContent = "테마를 선택하세요";
}

function selectedTheme() {
  return state.themes.find((theme) => theme.id === state.selectedThemeId);
}

function renderThemeCards(container, themes, clickHandler) {
  container.innerHTML = "";
  if (themes.length === 0) {
    renderEmpty(container, "표시할 테마가 없습니다.");
    return;
  }
  themes.forEach((theme) => container.appendChild(createThemeCard(theme, clickHandler)));
}

function createThemeCard(theme, clickHandler) {
  const card = document.createElement("button");
  card.type = "button";
  card.className = themeCardClassName(theme);
  card.addEventListener("click", () => clickHandler(theme.id));
  card.append(createThemeImage(theme), createThemeContent(theme));
  return card;
}

function themeCardClassName(theme) {
  if (theme.id === state.selectedThemeId) {
    return "theme-card is-selected";
  }
  return "theme-card";
}

function createThemeImage(theme) {
  const wrapper = document.createElement("div");
  wrapper.className = "theme-image";
  const image = document.createElement("img");
  image.src = theme.thumbnailUrl;
  image.alt = theme.name;
  image.addEventListener("error", () => replaceBrokenImage(wrapper, theme));
  wrapper.appendChild(image);
  return wrapper;
}

function replaceBrokenImage(wrapper, theme) {
  wrapper.textContent = theme.name.slice(0, 1);
}

function createThemeContent(theme) {
  const content = document.createElement("div");
  content.className = "theme-content";
  content.append(createTextElement("h3", theme.name));
  content.append(createTextElement("p", theme.description));
  content.append(createTextElement("span", selectLabel(theme)));
  content.lastElementChild.className = "select-label";
  return content;
}

function selectLabel(theme) {
  if (theme.id === state.selectedThemeId) {
    return "선택됨";
  }
  return "선택하기";
}

async function handleThemeSelect(themeId) {
  state.selectedThemeId = themeId;
  renderThemes();
  renderRankingThemes();
  await loadAvailableTimes();
}

async function handleDateChange(event) {
  state.selectedDate = event.target.value;
  await loadAvailableTimes();
}

function renderAvailableTimes() {
  elements.timeList.innerHTML = "";
  if (state.availableTimes.length === 0) {
    renderEmpty(elements.timeList, "선택 가능한 시간이 없습니다.");
    return;
  }
  state.availableTimes.forEach((time) => elements.timeList.appendChild(createTimeButton(time)));
}

function createTimeButton(timeAvailability) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = timeButtonClassName(timeAvailability);
  button.disabled = !timeAvailability.available;
  button.textContent = formatStartAt(timeAvailability.time.startAt);
  button.addEventListener("click", () => handleTimeSelect(timeAvailability.time.id));
  return button;
}

function timeButtonClassName(timeAvailability) {
  if (timeAvailability.time.id === state.selectedTimeId) {
    return "time-button is-selected";
  }
  return "time-button";
}

function handleTimeSelect(timeId) {
  state.selectedTimeId = timeId;
  renderAvailableTimes();
  updateSubmitButton();
}

async function handleReservationSubmit(event) {
  event.preventDefault();
  const reservation = await createReservation();
  renderReservationResult(reservation);
  showToast("예약이 완료되었습니다.");
  await loadAvailableTimes();
}

async function handleReservationSearch(event) {
  event.preventDefault();
  renderLoading(elements.searchResult, "예약을 조회하는 중입니다.");
  const reservations = await requestJson("/reservations");
  renderSearchResult(filterReservationsByName(reservations));
}

function filterReservationsByName(reservations) {
  const name = elements.searchNameInput.value.trim();
  return reservations.filter((reservation) => reservation.name === name);
}

function renderSearchResult(reservations) {
  elements.searchResult.innerHTML = "";
  if (reservations.length === 0) {
    renderEmpty(elements.searchResult, "조회된 예약이 없습니다.");
    return;
  }
  reservations.forEach((reservation) => {
    elements.searchResult.appendChild(createReservationItem(reservation));
  });
}

function createReservationItem(reservation) {
  const item = document.createElement("article");
  item.className = "reservation-item";
  item.append(createTextElement("strong", reservation.theme.name));
  item.append(createTextElement("span", reservation.name));
  item.append(createTextElement("span", reservation.date));
  item.append(createTextElement("span", formatStartAt(reservation.time.startAt)));
  item.append(createCancelButton(reservation));
  return item;
}

function createCancelButton(reservation) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "danger-button";
  button.textContent = "취소";
  button.addEventListener("click", () => confirmCancelReservation(reservation));
  return button;
}

async function confirmCancelReservation(reservation) {
  if (window.confirm(cancelConfirmMessage(reservation))) {
    await cancelReservation(reservation.id);
  }
}

function cancelConfirmMessage(reservation) {
  const themeName = reservation.theme.name;
  const startAt = formatStartAt(reservation.time.startAt);
  return `[${themeName} / ${reservation.date} / ${startAt}] 예약을 취소하시겠습니까?`;
}

async function cancelReservation(reservationId) {
  await request(`/reservations/${reservationId}`, {method: "DELETE"});
  showToast("예약이 취소되었습니다.");
  await refreshReservationSearch();
  await loadAvailableTimes();
}

async function refreshReservationSearch() {
  const reservations = await requestJson("/reservations");
  renderSearchResult(filterReservationsByName(reservations));
}

async function createReservation() {
  const payload = {
    name: elements.nameInput.value.trim(),
    date: state.selectedDate,
    timeId: state.selectedTimeId,
    themeId: state.selectedThemeId,
  };
  return requestJson("/reservations", createPostOption(payload));
}

function createPostOption(payload) {
  return {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(payload),
  };
}

function renderReservationResult(reservation) {
  elements.result.hidden = false;
  elements.result.innerHTML = `
    <h3>예약 완료</h3>
    <p>예약자: ${escapeHtml(reservation.name)}</p>
    <p>날짜: ${reservation.date}</p>
    <p>시간: ${formatStartAt(reservation.time.startAt)}</p>
    <p>테마: ${escapeHtml(reservation.theme.name)}</p>
  `;
}

function resetSelectedTime() {
  state.selectedTimeId = null;
}

function updateSubmitButton() {
  const disabled = !state.selectedThemeId || !state.selectedDate || !state.selectedTimeId;
  elements.submitButton.disabled = disabled;
}

async function requestJson(url, options = {}) {
  const response = await request(url, options);
  return response.json();
}

async function request(url, options = {}) {
  const response = await fetch(url, options);
  if (response.ok) {
    return response;
  }
  await handleErrorResponse(response);
}

async function handleErrorResponse(response) {
  const error = await readError(response);
  showToast(error.message, true);
  throw new Error(error.message);
}

async function readError(response) {
  const fallback = {message: "요청을 처리하지 못했습니다."};
  try {
    return response.json();
  } catch (error) {
    return fallback;
  }
}

function rankingUrl(period) {
  return `/themes/ranking?start-date=${period.startDate}&end-date=${period.endDate}`;
}

function availableTimeUrl() {
  return `/times/available?date=${state.selectedDate}&themeId=${state.selectedThemeId}`;
}

function recentWeekPeriod() {
  const endDate = addDays(new Date(), -1);
  const startDate = addDays(endDate, -6);
  return {
    startDate: formatDate(startDate),
    endDate: formatDate(endDate),
  };
}

function addDays(date, days) {
  const nextDate = new Date(date);
  nextDate.setDate(nextDate.getDate() + days);
  return nextDate;
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function formatStartAt(startAt) {
  return startAt.slice(0, 5);
}

function createTextElement(tagName, text) {
  const element = document.createElement(tagName);
  element.textContent = text;
  return element;
}

function renderLoading(container, message) {
  container.innerHTML = `<div class="loading">${message}</div>`;
}

function renderEmpty(container, message) {
  container.innerHTML = `<div class="empty">${message}</div>`;
}

function showToast(message, error = false) {
  elements.toast.textContent = message;
  elements.toast.className = error ? "toast is-error" : "toast";
  elements.toast.hidden = false;
  window.setTimeout(() => {
    elements.toast.hidden = true;
  }, 2400);
}

function escapeHtml(value) {
  return value
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}
