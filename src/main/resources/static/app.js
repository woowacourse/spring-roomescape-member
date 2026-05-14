const state = {
  reservations: [],
  myReservations: [],
  themes: [],
  times: [],
  availableTimes: [],
  editableTimesByReservationId: {},
  reservationDraftsById: {},
  popularThemes: [],
  myReservationName: "",
  availability: {
    date: "",
    themeId: "",
  },
};

const DEFAULT_THEME_IMAGE = "https://avatars.githubusercontent.com/u/177727543?v=4&size=64";

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => Array.from(document.querySelectorAll(selector));
let toastTimer;

document.addEventListener("DOMContentLoaded", async () => {
  setToday();
  renderTimeSelects();
  bindEvents();
  try {
    await loadAll();
  } catch (error) {
    showToast(getErrorMessage(error), true);
  } finally {
    route();
  }
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
    await safely("#availability-message", loadAvailableTimes);
  });

  $("#popular-theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await safely("#popular-theme-message", loadPopularThemes);
  });

  $("#reservation-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await safely("#reservation-message", createReservation);
  });

  $("#my-reservation-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await safely("#my-reservation-message", loadMyReservations);
  });

  $("#my-reservation-list").addEventListener("submit", updateMyReservation);
  $("#my-reservation-list").addEventListener("change", handleMyReservationChange);
  $("#my-reservation-list").addEventListener("click", handleMyReservationClick);

  $("#theme-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await safely("#theme-message", createTheme);
  });

  $("#time-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    await safely("#time-message", createTime);
  });

  $("[data-refresh='reservations']").addEventListener("click", () =>
    safely("#admin-reservation-message", loadReservations)
  );
  $("[data-refresh='my-reservations']").addEventListener("click", () =>
    safely("#my-reservation-message", loadMyReservations)
  );
  $("[data-refresh='themes']").addEventListener("click", () =>
    safely("#theme-list-message", loadThemes)
  );
  $("[data-refresh='times']").addEventListener("click", () =>
    safely("#time-list-message", loadTimes)
  );

  $("#reservation-date").addEventListener("change", clearAvailabilitySelection);
  $("#reservation-theme").addEventListener("change", clearAvailabilitySelection);
  $("#reservation-name").addEventListener("input", () => clearInlineMessage("#reservation-message"));
  $("#popular-recent-days").addEventListener("input", () => clearInlineMessage("#popular-theme-message"));
  $("#popular-limit").addEventListener("input", () => clearInlineMessage("#popular-theme-message"));
  $("#my-reservation-name").addEventListener("input", () => clearInlineMessage("#my-reservation-message"));
  $("#theme-name").addEventListener("input", () => clearInlineMessage("#theme-message"));
  $("#theme-description").addEventListener("input", () => clearInlineMessage("#theme-message"));
  $("#theme-thumbnail").addEventListener("input", () => clearInlineMessage("#theme-message"));
  $("#time-hour").addEventListener("change", () => clearInlineMessage("#time-message"));
  $("#time-minute").addEventListener("change", () => clearInlineMessage("#time-message"));
}

async function loadAll() {
  await Promise.all([loadReservations(), loadThemes(), loadTimes(), loadPopularThemes()]);
  renderHome();
  renderReservationOptions();
}

async function loadReservations() {
  state.reservations = await request("/admin/reservations");
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
    throw new Error("날짜와 테마를 선택해주세요.");
  }

  state.availableTimes = await request(`/times/available?date=${date}&themeId=${themeId}`);
  state.availability = { date, themeId };
  renderAvailableTimes();
  renderReservationCondition();
}

async function loadPopularThemes() {
  const recentDays = Number($("#popular-recent-days").value || 7);
  const limit = Number($("#popular-limit").value || 10);
  state.popularThemes = await request(`/themes/popular?recentDays=${recentDays}&limit=${limit}`);
  renderPopularThemes();
}

async function createReservation() {
  const { date, themeId } = state.availability;
  if (!date || !themeId) {
    throw new Error("가능 시간을 먼저 조회해주세요.");
  }

  if (!$("#selected-time").value) {
    throw new Error("예약할 시간을 선택해주세요.");
  }

  const payload = {
    name: $("#reservation-name").value.trim(),
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
  if (state.myReservationName && state.myReservationName === payload.name.trim()) {
    await loadMyReservations({ quiet: true });
  }
  showToast("예약이 생성되었습니다.");
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
  showToast("테마가 추가되었습니다.");
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
  showToast("시간이 추가되었습니다.");
}

async function deleteReservation(id) {
  await safely("#admin-reservation-message", async () => {
    await request(`/admin/reservations/${id}`, { method: "DELETE" });
    await loadReservations();
    if (state.myReservationName) {
      await loadMyReservations({ quiet: true });
    }
    showToast("예약이 취소되었습니다.");
  });
}

async function deleteTheme(id) {
  await safely("#theme-list-message", async () => {
    await request(`/admin/themes/${id}`, { method: "DELETE" });
    await loadThemes();
    showToast("테마가 삭제되었습니다.");
  });
}

async function deleteTime(id) {
  await safely("#time-list-message", async () => {
    await request(`/admin/times/${id}`, { method: "DELETE" });
    await loadTimes();
    showToast("시간이 삭제되었습니다.");
  });
}

async function loadMyReservations(options = {}) {
  const name = $("#my-reservation-name").value.trim() || state.myReservationName;
  if (!name) {
    throw new Error("예약자 이름을 입력해주세요.");
  }

  state.myReservationName = name;
  $("#my-reservation-name").value = name;
  state.myReservations = await request(`/reservations?name=${encodeURIComponent(name)}`);
  state.editableTimesByReservationId = {};
  state.reservationDraftsById = {};

  await Promise.all(
    state.myReservations.map((reservation) =>
      loadEditableTimesForReservation(reservation, reservation.date, { quiet: true })
    )
  );

  renderMyReservations();
  if (!options.quiet) {
    showToast(`${name}님의 예약 ${state.myReservations.length}건을 조회했습니다.`);
  }
}

async function loadEditableTimesForReservation(reservation, date, options = {}) {
  const reservationId = String(reservation.id);
  const themeId = reservation.theme?.id;
  const draft = getReservationDraft(reservation);
  draft.date = date;

  if (!themeId || !date) {
    state.editableTimesByReservationId[reservationId] = mergeTimes([reservation.time]);
    return;
  }

  let availableTimes = [];
  try {
    availableTimes = await request(
      `/times/available?date=${encodeURIComponent(date)}&themeId=${encodeURIComponent(themeId)}`,
      { quiet: options.quiet }
    );
  } catch (error) {
    if (!options.quiet) {
      throw error;
    }
  }
  const currentTime = date === reservation.date ? reservation.time : null;
  const editableTimes = mergeTimes([...availableTimes, currentTime]);

  state.editableTimesByReservationId[reservationId] = editableTimes;
  if (!editableTimes.some((time) => String(time.id) === String(draft.timeId))) {
    draft.timeId = editableTimes[0] ? String(editableTimes[0].id) : "";
  }
}

async function updateMyReservation(event) {
  const form = event.target.closest(".edit-reservation-form");
  if (!form) {
    return;
  }

  event.preventDefault();
  const messageSelector = reservationMessageSelector(form.dataset.reservationId);
  await safely(messageSelector, async () => {
    const reservation = findMyReservation(form.dataset.reservationId);
    if (!reservation) {
      throw new Error("예약 정보를 다시 조회해주세요.");
    }

    const draft = getReservationDraft(reservation);
    if (!draft.date || !draft.timeId) {
      throw new Error("변경할 날짜와 시간을 선택해주세요.");
    }

    await request(`/reservations/${reservation.id}`, {
      method: "PATCH",
      body: JSON.stringify({
        date: draft.date,
        timeId: Number(draft.timeId),
      }),
    });

    await Promise.all([loadReservations(), loadMyReservations({ quiet: true })]);
    showToast("예약이 변경되었습니다.");
  });
}

async function handleMyReservationChange(event) {
  const form = event.target.closest(".edit-reservation-form");
  if (!form) {
    return;
  }

  const reservation = findMyReservation(form.dataset.reservationId);
  if (!reservation) {
    return;
  }

  const draft = getReservationDraft(reservation);
  if (event.target.name === "date") {
    await safely(reservationMessageSelector(reservation.id), async () => {
      await loadEditableTimesForReservation(reservation, event.target.value);
      renderMyReservations();
    });
    return;
  }

  if (event.target.name === "timeId") {
    draft.timeId = event.target.value;
    clearInlineMessage(reservationMessageSelector(reservation.id));
  }
}

async function handleMyReservationClick(event) {
  const button = event.target.closest("[data-action='delete-my-reservation']");
  if (!button) {
    return;
  }

  await safely(reservationMessageSelector(button.dataset.reservationId), async () => {
    const reservation = findMyReservation(button.dataset.reservationId);
    if (!reservation) {
      throw new Error("예약 정보를 다시 조회해주세요.");
    }

    if (!window.confirm(`${reservation.date} ${formatTime(reservation.time?.startAt)} 예약을 취소할까요?`)) {
      return;
    }

    const requesterName = $("#my-reservation-name").value.trim() || state.myReservationName;
    if (!requesterName) {
      throw new Error("예약자 이름을 입력해주세요.");
    }

    await request(`/reservations/${reservation.id}?name=${encodeURIComponent(requesterName)}`, { method: "DELETE" });
    await Promise.all([loadReservations(), loadMyReservations({ quiet: true })]);
    showToast("예약이 취소되었습니다.");
  });
}

async function request(path, options = {}) {
  const fetchOptions = { ...options };
  delete fetchOptions.quiet;

  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    ...fetchOptions,
  });

  if (!response.ok) {
    throw new Error(await readErrorMessage(response));
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function readErrorMessage(response) {
  const fallbackMessage = `HTTP ${response.status}`;
  const contentType = response.headers.get("Content-Type") ?? "";

  if (contentType.includes("application/json")) {
    try {
      const body = await response.json();
      return body.message || fallbackMessage;
    } catch (error) {
      return fallbackMessage;
    }
  }

  const message = await response.text();
  return message || fallbackMessage;
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
      clearInlineMessage("#reservation-message");
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
  clearInlineMessage("#availability-message");
  clearInlineMessage("#reservation-message");
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

function renderMyReservations() {
  const container = $("#my-reservation-list");

  if (!state.myReservationName) {
    container.innerHTML = `<p class="empty inline">이름을 입력하면 예약 내역이 표시됩니다.</p>`;
    return;
  }

  container.innerHTML = state.myReservations.length
    ? state.myReservations.map(myReservationItem).join("")
    : `<p class="empty inline">${escapeHtml(state.myReservationName)}님의 예약이 없습니다.</p>`;
}

function myReservationItem(reservation) {
  const draft = getReservationDraft(reservation);
  const times = state.editableTimesByReservationId[String(reservation.id)] ?? mergeTimes([reservation.time]);
  const hasEditableTimes = times.length > 0;

  return `
    <article class="reservation-item">
      <div class="reservation-main">
        <div>
          <strong>${escapeHtml(reservation.theme?.name ?? "테마 없음")}</strong>
          <span>${escapeHtml(reservation.name)} · ${escapeHtml(reservation.date)} · ${formatTime(reservation.time?.startAt)}</span>
        </div>
        <span class="reservation-id">#${reservation.id}</span>
      </div>
      <p class="reservation-description">${escapeHtml(reservation.theme?.description ?? "")}</p>
      <form class="edit-reservation-form" data-reservation-id="${reservation.id}">
        <label>
          날짜
          <input name="date" type="date" value="${escapeHtml(draft.date)}" required>
        </label>
        <label>
          시간
          <select name="timeId" ${hasEditableTimes ? "" : "disabled"} required>
            ${hasEditableTimes ? timeOptions(times, draft.timeId) : `<option value="">선택 가능한 시간 없음</option>`}
          </select>
        </label>
        <button type="submit" ${hasEditableTimes ? "" : "disabled"}>변경</button>
        <button class="danger" type="button" data-action="delete-my-reservation" data-reservation-id="${reservation.id}">취소</button>
      </form>
      <p class="form-message card-message is-empty" data-message-for="reservation-${reservation.id}" aria-live="polite"></p>
    </article>
  `;
}

function renderThemes() {
  $("#theme-rows").innerHTML = state.themes.length
    ? state.themes.map((theme) => `
        <tr>
          <td>${theme.id}</td>
          <td>${escapeHtml(theme.name)}</td>
          <td>${escapeHtml(theme.description)}</td>
          <td>${theme.runtime}분</td>
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

function findMyReservation(id) {
  return state.myReservations.find((reservation) => String(reservation.id) === String(id));
}

function reservationMessageSelector(id) {
  return `[data-message-for="reservation-${id}"]`;
}

function getReservationDraft(reservation) {
  const reservationId = String(reservation.id);
  if (!state.reservationDraftsById[reservationId]) {
    state.reservationDraftsById[reservationId] = {
      date: reservation.date,
      timeId: String(reservation.time?.id ?? ""),
    };
  }

  return state.reservationDraftsById[reservationId];
}

function mergeTimes(times) {
  const uniqueTimes = new Map();
  times
    .filter(Boolean)
    .forEach((time) => uniqueTimes.set(String(time.id), time));

  return Array.from(uniqueTimes.values())
    .sort((left, right) => String(left.startAt).localeCompare(String(right.startAt)));
}

function timeOptions(times, selectedTimeId) {
  return times.map((time) => `
    <option value="${time.id}" ${String(time.id) === String(selectedTimeId) ? "selected" : ""}>
      ${formatTime(time.startAt)}
    </option>
  `).join("");
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
        <span>${theme.runtime}분</span>
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

async function safely(messageSelector, task) {
  clearInlineMessage(messageSelector);

  try {
    await task();
  } catch (error) {
    showInlineMessage(messageSelector, getErrorMessage(error), true);
  }
}

function showInlineMessage(selector, message, isError = true) {
  const messageElement = typeof selector === "string" ? $(selector) : selector;
  if (!messageElement) {
    showToast(message, isError);
    return;
  }

  messageElement.textContent = message;
  messageElement.classList.toggle("error", isError);
  messageElement.classList.toggle("success", !isError);
  messageElement.classList.remove("is-empty");
}

function clearInlineMessage(selector) {
  const messageElement = typeof selector === "string" ? $(selector) : selector;
  if (!messageElement) {
    return;
  }

  messageElement.textContent = "";
  messageElement.classList.remove("error", "success");
  messageElement.classList.add("is-empty");
}

function getErrorMessage(error) {
  if (error instanceof Error) {
    return error.message;
  }
  return String(error || "요청 처리 중 문제가 발생했습니다.");
}

function showToast(message, isError = false) {
  const toast = $("#reservation-toast");
  toast.textContent = message;
  toast.classList.toggle("error", isError);
  toast.hidden = false;

  window.clearTimeout(toastTimer);
  toastTimer = window.setTimeout(() => {
    toast.hidden = true;
  }, 750);
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
