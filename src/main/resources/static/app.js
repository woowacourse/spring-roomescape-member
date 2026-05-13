const state = {
  reservations: [],
  themes: [],
  times: [],
  availableTimes: [],
  popularThemes: [],
};

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => Array.from(document.querySelectorAll(selector));

document.addEventListener("DOMContentLoaded", async () => {
  setToday();
  renderTimeSelects();
  bindEvents();
  await runSafely(loadAll);
  route();
});

window.addEventListener("hashchange", route);

function setToday() {
  const today = new Date().toISOString().slice(0, 10);
  $("#reservation-date").value = today;
  $("#selected-date").value = today;
}

function bindEvents() {
  $("#availability-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await runSafely(loadAvailableTimes);
  });

  $("#popular-theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await runSafely(loadPopularThemes);
  });

  $("#reservation-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await runSafely(createReservation);
  });

  $("#theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await runSafely(createTheme);
  });

  $("#time-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await runSafely(createTime);
  });

  $("[data-refresh='reservations']").addEventListener("click", () => runSafely(loadReservations));
  $("[data-refresh='themes']").addEventListener("click", () => runSafely(loadThemes));
  $("[data-refresh='times']").addEventListener("click", () => runSafely(loadTimes));

  $("#selected-date").addEventListener("change", syncAvailabilityInputs);
  $("#selected-theme").addEventListener("change", syncAvailabilityInputs);
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
  $("#selected-date").value = date;
  $("#selected-theme").value = themeId;
  renderAvailableTimes();
  showNotice("예약 가능한 시간을 조회했습니다.");
}

async function loadPopularThemes() {
  state.popularThemes = await request("/themes?days=7&limits=10");
  renderPopularThemes();
  showNotice("인기 테마를 조회했습니다.");
}

async function createReservation() {
  if (!$("#selected-time").value) {
    showNotice("예약할 시간을 선택해주세요.", true);
    return;
  }

  const payload = {
    name: $("#reservation-name").value,
    date: $("#selected-date").value,
    timeId: Number($("#selected-time").value),
    themeId: Number($("#selected-theme").value),
  };

  await request("/reservations", {
    method: "POST",
    body: JSON.stringify(payload),
  });

  $("#reservation-form").reset();
  setToday();
  state.availableTimes = [];
  renderAvailableTimes();
  await loadReservations();
  showNotice("예약이 생성되었습니다.");
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
  showNotice("테마가 추가되었습니다.");
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
  showNotice("시간이 추가되었습니다.");
}

async function deleteReservation(id) {
  await runSafely(async () => {
    await request(`/reservations/${id}`, { method: "DELETE" });
    await loadReservations();
    showNotice("예약이 삭제되었습니다.");
  });
}

async function deleteTheme(id) {
  await runSafely(async () => {
    await request(`/admin/themes/${id}`, { method: "DELETE" });
    await loadThemes();
    showNotice("테마가 삭제되었습니다.");
  });
}

async function deleteTime(id) {
  await runSafely(async () => {
    await request(`/admin/times/${id}`, { method: "DELETE" });
    await loadTimes();
    showNotice("시간이 삭제되었습니다.");
  });
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
    const message = await readErrorMessage(response);
    showNotice(message, true);
    const error = new Error(message);
    error.noticeShown = true;
    throw error;
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function runSafely(action) {
  try {
    await action();
  } catch (error) {
    if (!error.noticeShown) {
      showNotice(error.message || "요청을 처리할 수 없습니다.", true);
    }
    console.error(error);
  }
}

async function readErrorMessage(response) {
  const fallbackMessage = `요청 실패: ${response.status}`;
  const contentType = response.headers.get("Content-Type") || "";

  if (contentType.includes("application/json")) {
    const body = await response.json().catch(() => null);
    return body?.message || fallbackMessage;
  }

  const body = await response.text().catch(() => "");
  return body || fallbackMessage;
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
  const themeOptions = [
    `<option value="">테마 선택</option>`,
    ...state.themes.map((theme) => `<option value="${theme.id}">${escapeHtml(theme.name)}</option>`),
  ].join("");

  $("#reservation-theme").innerHTML = themeOptions;
  $("#selected-theme").innerHTML = themeOptions;
  renderAvailableTimes();
}

function renderAvailableTimes() {
  const buttons = state.availableTimes.map((time, index) => (
    `<button class="time-chip${index === 0 ? " selected" : ""}" type="button" data-time-id="${time.id}">
      ${formatTime(time.startAt)}
    </button>`
  ));

  $("#available-time-list").innerHTML = buttons.length
    ? buttons.join("")
    : `<p class="empty inline">예약 가능한 시간이 없습니다.</p>`;

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

function syncAvailabilityInputs() {
  $("#reservation-date").value = $("#selected-date").value;
  $("#reservation-theme").value = $("#selected-theme").value;
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
