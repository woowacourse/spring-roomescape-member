import { api } from "./api.js";
import { appEl, modalRootEl, toastRootEl } from "./dom.js";
import { ADMIN_TABS, ROUTES, state, tomorrowString } from "./state.js";
import { render } from "./render.js";

window.addEventListener("hashchange", handleRoute);
document.addEventListener("click", handleClick);
document.addEventListener("submit", handleSubmit);
document.addEventListener("change", handleChange);
document.addEventListener("input", handleInput);
document.addEventListener("error", handleImageError, true);
window.addEventListener("resize", () => requestAnimationFrame(render));

handleRoute();

function handleRoute() {
  const route = parseRoute();

  if (route.role === "home") {
    state.role = null;
    render();
    return;
  }

  state.role = route.role;
  state.adminTab = route.adminTab;
  render();

  if (state.role === "user") {
    loadUserData();
    return;
  }

  loadAdminDashboard();
}

function parseRoute() {
  const normalized = location.hash.replace(/^#\/?/, "");
  const [role = "home", tab = "themes"] = normalized.split("/");

  if (!ROUTES.has(role)) {
    return { role: "home", adminTab: "themes" };
  }

  if (role !== "admin") {
    return { role, adminTab: "themes" };
  }

  return {
    role,
    adminTab: ADMIN_TABS.has(tab) ? tab : "themes"
  };
}

async function handleClick(event) {
  const target = event.target.closest("[data-action]");
  if (!target || target.disabled) {
    return;
  }

  const action = target.dataset.action;

  if (action === "choose-role") {
    location.hash = target.dataset.role === "admin" ? "#/admin/themes" : "#/user";
    return;
  }

  if (action === "go-home") {
    location.hash = "#/";
    return;
  }

  if (action === "refresh-user") {
    await loadUserData({ force: true });
    return;
  }

  if (action === "select-theme") {
    state.selectedThemeId = Number(target.dataset.themeId);
    state.selectedTimeId = null;
    render();
    await loadAvailableTimes();
    scrollReservationSheetIntoView();
    return;
  }

  if (action === "select-time") {
    state.selectedTimeId = Number(target.dataset.timeId);
    render();
    scrollReservationSheetIntoView();
    return;
  }

  if (action === "admin-tab") {
    location.hash = `#/admin/${target.dataset.tab}`;
    return;
  }

  if (action === "refresh-admin") {
    await loadAdminData(state.adminTab, { force: true });
    return;
  }

  if (action === "delete-theme") {
    openConfirm("테마를 삭제할까요?", "이 테마를 사용하는 예약이 있으면 서버에서 삭제를 거부할 수 있습니다.", () => deleteTheme(Number(target.dataset.themeId)));
    return;
  }

  if (action === "delete-time") {
    openConfirm("예약 시간을 삭제할까요?", "이 시간을 사용하는 예약이 있으면 서버에서 삭제를 거부할 수 있습니다.", () => deleteTime(Number(target.dataset.timeId)));
    return;
  }

  if (action === "delete-reservation") {
    openConfirm("예약을 삭제할까요?", "삭제한 예약은 현재 화면에서 즉시 사라집니다.", () => deleteReservation(Number(target.dataset.reservationId)));
    return;
  }

  if (action === "confirm-cancel") {
    if (event.target === target || target.closest(".confirm-actions")) {
      closeConfirm();
    }
    return;
  }

  if (action === "confirm-ok") {
    const onConfirm = state.confirm?.onConfirm;
    closeConfirm();
    if (onConfirm) {
      await onConfirm();
    }
  }
}

async function handleSubmit(event) {
  event.preventDefault();

  const form = event.target;

  if (form.id === "reservation-form") {
    await submitReservation(form);
    return;
  }

  if (form.id === "theme-form") {
    await submitTheme(form);
    return;
  }

  if (form.id === "time-form") {
    await submitTime(form);
  }
}

async function handleChange(event) {
  if (event.target.id !== "reservation-date") {
    return;
  }

  state.selectedDate = event.target.value || tomorrowString();
  state.selectedTimeId = null;
  render();
  await loadAvailableTimes();
}

function handleInput(event) {
  if (event.target.id === "reservation-name") {
    state.reservationName = event.target.value;
    return;
  }

  if (event.target.id === "theme-name") {
    state.themeDraft.name = event.target.value;
    return;
  }

  if (event.target.id === "theme-description") {
    state.themeDraft.description = event.target.value;
    return;
  }

  if (event.target.id === "theme-thumbnail") {
    state.themeDraft.thumbnailImgUrl = event.target.value;
    return;
  }

  if (event.target.id === "time-start-at") {
    state.timeDraft.startAt = event.target.value;
  }
}

function handleImageError(event) {
  if (!event.target.matches("img[data-fallback-image]")) {
    return;
  }

  const wrapper = event.target.closest(".theme-thumb");
  if (wrapper) {
    wrapper.classList.add("image-failed");
    wrapper.classList.remove("has-image");
  }
}

async function loadUserData(options = {}) {
  if (state.loading.user && !options.force) {
    return;
  }

  state.loading.user = true;
  render();

  try {
    const [themes, popularThemes] = await Promise.all([
      api("/themes"),
      api("/themes/popular-top-10")
    ]);

    state.themes = themes;
    state.popularThemes = popularThemes;

    if (state.selectedThemeId && !state.themes.some((theme) => Number(theme.id) === Number(state.selectedThemeId))) {
      state.selectedThemeId = null;
      state.selectedTimeId = null;
      state.availableTimes = [];
    }
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.loading.user = false;
    render();
  }
}

async function loadAvailableTimes() {
  if (!state.selectedThemeId || !state.selectedDate) {
    return;
  }

  state.loading.times = true;
  render();

  try {
    const params = new URLSearchParams({
      themeId: state.selectedThemeId,
      date: state.selectedDate
    });
    state.availableTimes = await api(`/times?${params.toString()}`);

    if (!state.availableTimes.some((time) => Number(time.id) === Number(state.selectedTimeId) && time.available)) {
      state.selectedTimeId = null;
    }
  } catch (error) {
    state.availableTimes = [];
    showToast(error.message, "error");
  } finally {
    state.loading.times = false;
    render();
  }
}

async function loadAdminData(tab, options = {}) {
  if (tab === "themes") {
    await loadAdminThemes(options);
    return;
  }

  if (tab === "times") {
    await loadAdminTimes(options);
    return;
  }

  await loadReservations(options);
}

async function loadAdminDashboard(options = {}) {
  await Promise.all([
    loadAdminThemes(options),
    loadAdminTimes(options),
    loadReservations(options)
  ]);
}

async function loadAdminThemes(options = {}) {
  if (state.loading.adminThemes && !options.force) {
    return;
  }

  state.loading.adminThemes = true;
  render();

  try {
    state.themes = await api("/themes");
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.loading.adminThemes = false;
    render();
  }
}

async function loadAdminTimes(options = {}) {
  if (state.loading.adminTimes && !options.force) {
    return;
  }

  state.loading.adminTimes = true;
  render();

  try {
    state.adminTimes = await api("/admin/times");
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.loading.adminTimes = false;
    render();
  }
}

async function loadReservations(options = {}) {
  if (state.loading.reservations && !options.force) {
    return;
  }

  state.loading.reservations = true;
  render();

  try {
    state.reservations = await api("/reservations");
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.loading.reservations = false;
    render();
  }
}

async function submitReservation(form) {
  const formData = new FormData(form);
  const name = String(formData.get("name") || "").trim();

  if (!state.selectedThemeId) {
    showToast("테마를 선택해 주세요.", "error");
    return;
  }

  if (!state.selectedTimeId) {
    showToast("예약 시간을 선택해 주세요.", "error");
    return;
  }

  if (!name) {
    showToast("예약자 이름을 입력해 주세요.", "error");
    return;
  }

  state.submitting = true;
  render();

  try {
    await api("/reservations", {
      method: "POST",
      body: {
        name,
        date: state.selectedDate,
        themeId: state.selectedThemeId,
        timeId: state.selectedTimeId
      }
    });

    state.reservationName = "";
    state.selectedTimeId = null;
    showToast("예약이 완료되었습니다.", "success");
    await loadAvailableTimes();
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.submitting = false;
    render();
  }
}

async function submitTheme(form) {
  const formData = new FormData(form);
  const payload = {
    name: String(formData.get("name") || "").trim(),
    description: String(formData.get("description") || "").trim(),
    thumbnailImgUrl: String(formData.get("thumbnailImgUrl") || "").trim()
  };

  if (!payload.name || !payload.description || !payload.thumbnailImgUrl) {
    showToast("테마 이름, 설명, 썸네일 URL을 모두 입력해 주세요.", "error");
    return;
  }

  state.submitting = true;
  render();

  try {
    await api("/admin/themes", {
      method: "POST",
      body: payload
    });

    state.themeDraft = {
      name: "",
      description: "",
      thumbnailImgUrl: ""
    };
    showToast("테마가 추가되었습니다.", "success");
    await loadAdminThemes({ force: true });
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.submitting = false;
    render();
  }
}

async function submitTime(form) {
  const formData = new FormData(form);
  const startAt = String(formData.get("startAt") || "").trim();

  if (!startAt) {
    showToast("예약 시간을 입력해 주세요.", "error");
    return;
  }

  state.submitting = true;
  render();

  try {
    await api("/admin/times", {
      method: "POST",
      body: { startAt }
    });

    state.timeDraft = {
      startAt: ""
    };
    showToast("예약 시간이 추가되었습니다.", "success");
    await loadAdminTimes({ force: true });
  } catch (error) {
    showToast(error.message, "error");
  } finally {
    state.submitting = false;
    render();
  }
}

async function deleteTheme(themeId) {
  try {
    await api(`/admin/themes/${themeId}`, { method: "DELETE" });
    showToast("테마가 삭제되었습니다.", "success");
    await loadAdminThemes({ force: true });
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function deleteTime(timeId) {
  try {
    await api(`/admin/times/${timeId}`, { method: "DELETE" });
    showToast("예약 시간이 삭제되었습니다.", "success");
    await loadAdminTimes({ force: true });
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function deleteReservation(reservationId) {
  try {
    await api(`/reservations/${reservationId}`, { method: "DELETE" });
    showToast("예약이 삭제되었습니다.", "success");
    await loadReservations({ force: true });
  } catch (error) {
    showToast(error.message, "error");
  }
}

function openConfirm(title, message, onConfirm) {
  state.confirm = { title, message, onConfirm };
  render();
  requestAnimationFrame(() => modalRootEl.querySelector(".confirm-sheet")?.focus());
}

function closeConfirm() {
  state.confirm = null;
  render();
}

function scrollReservationSheetIntoView() {
  requestAnimationFrame(() => {
    appEl.querySelector("#reservation-sheet")?.scrollIntoView({ behavior: "smooth", block: "center" });
  });
}

function showToast(message, type = "default") {
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  toastRootEl.appendChild(toast);

  window.setTimeout(() => {
    toast.remove();
  }, 3600);
}
