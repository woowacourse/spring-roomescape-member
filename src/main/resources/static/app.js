const state = {
  reservations: [],
  themes: [],
  times: [],
  availableTimes: [],
  popularThemes: [],
  availability: {
    date: "",
    themeId: "",
  },
};

const DEFAULT_THEME_IMAGE = "https://avatars.githubusercontent.com/u/177727543?v=4&size=64";

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => Array.from(document.querySelectorAll(selector));
let reservationToastTimer;

document.addEventListener("DOMContentLoaded", async () => {
  setToday();
  renderTimeSelects();
  bindEvents();
  await loadAll();
  route();
});

window.addEventListener("hashchange", route);
window.addEventListener("error", replaceBrokenThemeImage, true);

function setToday() {
  const today = new Date().toISOString().slice(0, 10);
  $("#reservation-date").value = today;
}

function bindEvents() {
  $("#availability-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadAvailableTimes();
  });

  $("#popular-theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadPopularThemes();
  });

  $("#reservation-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await createReservation();
  });

  $("#theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await createTheme();
  });

  $("#time-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await createTime();
  });

  $("[data-refresh='reservations']").addEventListener("click", loadReservations);
  $("[data-refresh='themes']").addEventListener("click", loadThemes);
  $("[data-refresh='times']").addEventListener("click", loadTimes);

  $("#reservation-date").addEventListener("change", clearAvailabilitySelection);
  $("#reservation-theme").addEventListener("change", clearAvailabilitySelection);
}

async function loadAll() {
  await Promise.all([loadReservations(), loadThemes(), loadTimes()]);
  renderHome();
  renderReservationOptions();
}

async function loadReservations() {
  state.reservations = await request("/reservations");
  renderReservations();
  renderHome();
}

async function loadThemes() {
  state.themes = await request("/themes");
  renderThemes();
  renderHome();
  renderReservationOptions();
}

async function loadTimes() {
  state.times = await request("/times");
  renderTimes();
  renderHome();
}

async function loadAvailableTimes() {
  const date = $("#reservation-date").value;
  const themeId = $("#reservation-theme").value;
  if (!date || !themeId) {
    showNotice("날짜와 테마를 선택해주세요.", true);
    return;
  }

  state.availableTimes = await request(`/times?date=${date}&themeId=${themeId}`);
  state.availability = { date, themeId };
  renderAvailableTimes();
  renderReservationCondition();
}

async function loadPopularThemes() {
  state.popularThemes = await request("/themes?days=7&limits=10");
  renderPopularThemes();
}

async function createReservation() {
  const { date, themeId } = state.availability;
  if (!date || !themeId) {
    showNotice("가능 시간을 먼저 조회해주세요.", true);
    return;
  }

  if (!$("#selected-time").value) {
    showNotice("예약할 시간을 선택해주세요.", true);
    return;
  }

  const payload = {
    name: $("#reservation-name").value,
    date,
    timeId: Number($("#selected-time").value),
    themeId: Number(themeId),
  };

  await request("/reservations", {
    method: "POST",
    body: JSON.stringify(payload),
  });

  $("#reservation-form").reset();
  setToday();
  $("#reservation-theme").value = "";
  clearAvailabilitySelection();
  await loadReservations();
  showReservationToast("예약이 생성되었습니다.");
}

async function createTheme() {
  const payload = {
    name: $("#theme-name").value,
    description: $("#theme-description").value,
    thumbnailUrl: $("#theme-thumbnail").value,
  };

  await request("/admin/themes", {
    method: "POST",
    body: JSON.stringify(payload),
  });

  $("#theme-form").reset();
  await loadThemes();
}

async function createTime() {
  const payload = {
    startAt: `${$("#time-hour").value}:${$("#time-minute").value}`,
  };

  await request("/admin/times", {
    method: "POST",
    body: JSON.stringify(payload),
  });

  $("#time-form").reset();
  await loadTimes();
}

async function deleteReservation(id) {
  await request(`/reservations/${id}`, { method: "DELETE" });
  await loadReservations();
}

async function deleteTheme(id) {
  await request(`/admin/themes/${id}`, { method: "DELETE" });
  await loadThemes();
}

async function deleteTime(id) {
  await request(`/admin/times/${id}`, { method: "DELETE" });
  await loadTimes();
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text();
    showNotice(message || `요청 실패: ${response.status}`, true);
    throw new Error(message || `HTTP ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

function route() {
  const routeName = window.location.hash.replace("#", "") || "home";
  const pageName = document.querySelector(`[data-page="${routeName}"]`) ? routeName : "home";

  $$(".page").forEach((page) => {
    page.hidden = page.dataset.page !== pageName;
  });

  $$(".tabs a").forEach((link) => {
    link.classList.toggle("active", link.dataset.route === pageName);
  });
}

function renderHome() {
  $("#theme-count").textContent = state.themes.length;
  $("#time-count").textContent = state.times.length;
  $("#reservation-count").textContent = state.reservations.length;

  $("#theme-cards").innerHTML = state.themes.length
    ? state.themes.map(themeCard).join("")
    : empty("등록된 테마가 없습니다.");
}

function renderPopularThemes() {
  $("#popular-theme-cards").innerHTML = state.popularThemes.length
    ? state.popularThemes.map(themeCard).join("")
    : empty("조건에 맞는 인기 테마가 없습니다.");
}

function renderReservationOptions() {
  const currentThemeId = $("#reservation-theme").value;
  const themeOptions = [
    `<option value="">테마 선택</option>`,
    ...state.themes.map((theme) => `<option value="${theme.id}">${escapeHtml(theme.name)}</option>`),
  ].join("");

  $("#reservation-theme").innerHTML = themeOptions;
  if (state.themes.some((theme) => String(theme.id) === currentThemeId)) {
    $("#reservation-theme").value = currentThemeId;
  }

  renderAvailableTimes();
  renderReservationCondition();
}

function renderAvailableTimes() {
  const buttons = state.availableTimes.map((time, index) => (
    `<button class="time-chip${index === 0 ? " selected" : ""}" type="button" data-time-id="${time.id}">
      ${formatTime(time.startAt)}
    </button>`
  ));

  const emptyMessage = state.availability.date
    ? "예약 가능한 시간이 없습니다."
    : "날짜와 테마를 선택한 뒤 가능 시간 조회를 눌러주세요.";

  $("#available-time-list").innerHTML = buttons.length
    ? buttons.join("")
    : `<p class="empty inline">${emptyMessage}</p>`;

  if (state.availableTimes.length > 0) {
    $("#selected-time").value = String(state.availableTimes[0].id);
  } else {
    $("#selected-time").value = "";
  }

  $$("#available-time-list .time-chip").forEach((button) => {
    button.addEventListener("click", () => {
      $("#selected-time").value = button.dataset.timeId;
      $$("#available-time-list .time-chip").forEach((chip) => chip.classList.remove("selected"));
      button.classList.add("selected");
    });
  });
}

function renderReservationCondition() {
  const condition = $("#reservation-condition");
  const { date, themeId } = state.availability;
  const theme = state.themes.find((item) => String(item.id) === String(themeId));

  if (!date || !theme) {
    condition.textContent = "조회 조건 없음";
    condition.classList.add("empty-condition");
    return;
  }

  condition.innerHTML = `
    <span>${escapeHtml(date)}</span>
    <span>${escapeHtml(theme.name)}</span>
  `;
  condition.classList.remove("empty-condition");
}

function clearAvailabilitySelection() {
  state.availableTimes = [];
  state.availability = { date: "", themeId: "" };
  $("#selected-time").value = "";
  renderAvailableTimes();
  renderReservationCondition();
}

function renderReservations() {
  $("#reservation-rows").innerHTML = state.reservations.length
    ? state.reservations.map((reservation) => `
        <tr>
          <td>${reservation.id}</td>
          <td>${escapeHtml(reservation.name)}</td>
          <td>${reservation.date}</td>
          <td>${escapeHtml(reservation.theme?.name ?? "-")}</td>
          <td>${formatTime(reservation.time?.startAt)}</td>
          <td><button class="danger" type="button" onclick="deleteReservation(${reservation.id})">삭제</button></td>
        </tr>
      `).join("")
    : `<tr><td colspan="6" class="empty">예약이 없습니다.</td></tr>`;
}

function renderThemes() {
  $("#theme-rows").innerHTML = state.themes.length
    ? state.themes.map((theme) => `
        <tr>
          <td>${theme.id}</td>
          <td>${escapeHtml(theme.name)}</td>
          <td>${escapeHtml(theme.description)}</td>
          <td>${theme.runtime}시간</td>
          <td><button class="danger" type="button" onclick="deleteTheme(${theme.id})">삭제</button></td>
        </tr>
      `).join("")
    : `<tr><td colspan="5" class="empty">테마가 없습니다.</td></tr>`;
}

function renderTimes() {
  $("#time-rows").innerHTML = state.times.length
    ? state.times.map((time) => `
        <tr>
          <td>${time.id}</td>
          <td>${formatTime(time.startAt)}</td>
          <td><button class="danger" type="button" onclick="deleteTime(${time.id})">삭제</button></td>
        </tr>
      `).join("")
    : `<tr><td colspan="3" class="empty">시간이 없습니다.</td></tr>`;
}

function renderTimeSelects() {
  $("#time-hour").innerHTML = rangeOptions(0, 23);
  $("#time-minute").innerHTML = rangeOptions(0, 59);
}

function themeCard(theme) {
  return `
    <article class="theme-card">
      <img src="${escapeHtml(theme.thumbnailUrl)}" alt="">
      <div>
        <h3>${escapeHtml(theme.name)}</h3>
        <p>${escapeHtml(theme.description)}</p>
        <span>${theme.runtime}시간</span>
      </div>
    </article>
  `;
}

function replaceBrokenThemeImage(event) {
  const image = event.target;
  if (!(image instanceof HTMLImageElement) || !image.closest(".theme-card")) {
    return;
  }

  if (image.dataset.fallbackApplied === "true") {
    return;
  }

  image.dataset.fallbackApplied = "true";
  image.src = DEFAULT_THEME_IMAGE;
}

function showReservationToast(message) {
  const toast = $("#reservation-toast");
  toast.textContent = message;
  toast.hidden = false;

  window.clearTimeout(reservationToastTimer);
  reservationToastTimer = window.setTimeout(() => {
    toast.hidden = true;
  }, 750);
}

function showNotice(message, isError = false) {
  const notice = $("#notice");
  notice.textContent = message;
  notice.classList.toggle("error", isError);
  notice.hidden = false;

  window.setTimeout(() => {
    notice.hidden = true;
  }, 2800);
}

function empty(message) {
  return `<p class="empty">${escapeHtml(message)}</p>`;
}

function formatTime(value) {
  if (!value) {
    return "-";
  }
  return String(value).slice(0, 5);
}

function rangeOptions(start, end) {
  const options = [];
  for (let value = start; value <= end; value += 1) {
    const label = String(value).padStart(2, "0");
    options.push(`<option value="${label}">${label}</option>`);
  }
  return options.join("");
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}
