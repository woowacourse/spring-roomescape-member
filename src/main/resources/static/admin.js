const adminState = {
  reservations: [],
  themes: [],
  times: [],
  reservationPage: 1,
  reservationPageSize: 10,
  changingReservation: null,
  changeSelectedTimeId: null,
};

const adminElements = {
  reservationCount: document.querySelector("#reservation-count"),
  themeCount: document.querySelector("#theme-count"),
  timeCount: document.querySelector("#time-count"),
  reservationFilterName: document.querySelector("#reservation-filter-name"),
  reservationFilterDate: document.querySelector("#reservation-filter-date"),
  reservationFilterTheme: document.querySelector("#reservation-filter-theme"),
  reservationFilterReset: document.querySelector("#reservation-filter-reset"),
  reservationTableBody: document.querySelector("#reservation-table-body"),
  reservationPagination: document.querySelector("#reservation-pagination"),
  themeForm: document.querySelector("#theme-form"),
  themeName: document.querySelector("#theme-name"),
  themeDescription: document.querySelector("#theme-description"),
  themeThumbnail: document.querySelector("#theme-thumbnail"),
  themeAdminList: document.querySelector("#theme-admin-list"),
  timeForm: document.querySelector("#time-form"),
  timeStartAt: document.querySelector("#time-start-at"),
  timeAdminList: document.querySelector("#time-admin-list"),
  toast: document.querySelector("#toast"),
  // 에러 모달
  errorModal: document.querySelector("#error-modal"),
  errorModalBody: document.querySelector("#error-modal-body"),
  errorModalClose: document.querySelector("#error-modal-close"),
  // 확인 모달
  confirmModal: document.querySelector("#confirm-modal"),
  confirmModalBody: document.querySelector("#confirm-modal-body"),
  confirmModalOk: document.querySelector("#confirm-modal-ok"),
  confirmModalCancel: document.querySelector("#confirm-modal-cancel"),
  // 변경 모달
  changeModal: document.querySelector("#change-modal"),
  changeThemeSelect: document.querySelector("#change-theme"),
  changeDateInput: document.querySelector("#change-date"),
  changeTimeList: document.querySelector("#change-time-list"),
  changeModalCancel: document.querySelector("#change-modal-cancel"),
  changeModalConfirm: document.querySelector("#change-modal-confirm"),
};

document.addEventListener("DOMContentLoaded", initializeAdmin);

async function initializeAdmin() {
  bindAdminEvents();
  await loadAdminData();
}

function bindAdminEvents() {
  adminElements.reservationFilterName.addEventListener("input", handleReservationFilterChange);
  adminElements.reservationFilterDate.addEventListener("change", handleReservationFilterChange);
  adminElements.reservationFilterTheme.addEventListener("change", handleReservationFilterChange);
  adminElements.reservationFilterReset.addEventListener("click", resetReservationFilters);
  adminElements.themeForm.addEventListener("submit", handleThemeSubmit);
  adminElements.timeForm.addEventListener("submit", handleTimeSubmit);

  // 에러 모달
  adminElements.errorModalClose.addEventListener("click", closeErrorModal);
  adminElements.errorModal.addEventListener("click", (e) => {
    if (e.target === adminElements.errorModal) closeErrorModal();
  });

  // 변경 모달
  adminElements.changeThemeSelect.addEventListener("change", handleChangeThemeChange);
  adminElements.changeDateInput.addEventListener("change", handleChangeDateChange);
  adminElements.changeModalCancel.addEventListener("click", closeChangeModal);
  adminElements.changeModal.addEventListener("click", (e) => {
    if (e.target === adminElements.changeModal) closeChangeModal();
  });
  adminElements.changeModalConfirm.addEventListener("click", handleChangeConfirm);

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      closeErrorModal();
      closeChangeModal();
      closeConfirmModal();
    }
  });
}

// ── 에러 모달 ────────────────────────────────────────────

function showErrorModal(message) {
  adminElements.errorModalBody.textContent = message;
  adminElements.errorModal.hidden = false;
  adminElements.errorModalClose.focus();
}

function closeErrorModal() {
  adminElements.errorModal.hidden = true;
}

// ── 확인 모달 (window.confirm 대체) ──────────────────────

let confirmResolve = null;

function showConfirmModal(message) {
  return new Promise((resolve) => {
    confirmResolve = resolve;
    adminElements.confirmModalBody.textContent = message;
    adminElements.confirmModal.hidden = false;
    adminElements.confirmModalOk.focus();

    adminElements.confirmModalOk.onclick = () => {
      closeConfirmModal();
      resolve(true);
    };
    adminElements.confirmModalCancel.onclick = () => {
      closeConfirmModal();
      resolve(false);
    };
  });
}

function closeConfirmModal() {
  adminElements.confirmModal.hidden = true;
  confirmResolve = null;
}

// ── 데이터 로드 ──────────────────────────────────────────

async function loadAdminData() {
  const [reservations, themes, times] = await Promise.all([
    requestJson("/reservations"),
    requestJson("/themes"),
    requestJson("/admin/times"),
  ]);
  if (!reservations || !themes || !times) return;
  adminState.reservations = reservations;
  adminState.themes = themes;
  adminState.times = times;
  renderAdminPage();
}

function renderAdminPage() {
  renderSummary();
  renderThemeFilter();
  renderReservations();
  renderThemes();
  renderTimes();
  populateChangeThemeSelect();
}

// ── 요약 현황 ────────────────────────────────────────────

function renderSummary() {
  adminElements.reservationCount.textContent = adminState.reservations.length;
  adminElements.themeCount.textContent = adminState.themes.length;
  adminElements.timeCount.textContent = adminState.times.length;
}

// ── 예약 목록 ────────────────────────────────────────────

function renderThemeFilter() {
  const selectedValue = adminElements.reservationFilterTheme.value;
  adminElements.reservationFilterTheme.innerHTML = '<option value="">전체</option>';
  adminState.themes.forEach((theme) => {
    const option = document.createElement("option");
    option.value = String(theme.id);
    option.textContent = theme.name;
    adminElements.reservationFilterTheme.appendChild(option);
  });
  adminElements.reservationFilterTheme.value = selectedValue;
}

function renderReservations() {
  adminElements.reservationTableBody.innerHTML = "";
  const reservations = filteredReservations();
  if (reservations.length === 0) {
    renderTableEmpty(adminElements.reservationTableBody, 6, "조회된 예약이 없습니다.");
    renderReservationPagination(reservations);
    return;
  }
  normalizeReservationPage(reservations.length);
  pagedReservations(reservations).forEach((reservation) => {
    adminElements.reservationTableBody.appendChild(createReservationRow(reservation));
  });
  renderReservationPagination(reservations);
}

function handleReservationFilterChange() {
  adminState.reservationPage = 1;
  renderReservations();
}

function pagedReservations(reservations) {
  const startIndex = (adminState.reservationPage - 1) * adminState.reservationPageSize;
  const endIndex = startIndex + adminState.reservationPageSize;
  return reservations.slice(startIndex, endIndex);
}

function normalizeReservationPage(reservationCount) {
  const totalPages = reservationTotalPages(reservationCount);
  if (adminState.reservationPage <= totalPages) return;
  adminState.reservationPage = totalPages;
}

function renderReservationPagination(reservations) {
  adminElements.reservationPagination.innerHTML = "";
  if (reservations.length === 0) return;
  adminElements.reservationPagination.append(
    createPaginationButton("이전", adminState.reservationPage - 1, isFirstReservationPage()),
    createPaginationSummary(reservations.length),
    createPaginationButton("다음", adminState.reservationPage + 1, isLastReservationPage(reservations.length))
  );
}

function createPaginationButton(text, page, disabled) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "secondary-button";
  button.disabled = disabled;
  button.textContent = text;
  button.addEventListener("click", () => moveReservationPage(page));
  return button;
}

function createPaginationSummary(reservationCount) {
  const summary = document.createElement("span");
  summary.className = "pagination-summary";
  summary.textContent = `${adminState.reservationPage} / ${reservationTotalPages(reservationCount)} 페이지`;
  return summary;
}

function moveReservationPage(page) {
  adminState.reservationPage = page;
  renderReservations();
}

function isFirstReservationPage() {
  return adminState.reservationPage === 1;
}

function isLastReservationPage(reservationCount) {
  return adminState.reservationPage === reservationTotalPages(reservationCount);
}

function reservationTotalPages(reservationCount) {
  return Math.ceil(reservationCount / adminState.reservationPageSize);
}

function filteredReservations() {
  return adminState.reservations.filter((reservation) => {
    return includesName(reservation) && matchesDate(reservation) && matchesTheme(reservation);
  });
}

function includesName(reservation) {
  const name = adminElements.reservationFilterName.value.trim();
  return reservation.name.includes(name);
}

function matchesDate(reservation) {
  const date = adminElements.reservationFilterDate.value;
  if (date === "") return true;
  return reservation.date === date;
}

function matchesTheme(reservation) {
  const themeId = adminElements.reservationFilterTheme.value;
  if (themeId === "") return true;
  return String(reservation.theme.id) === themeId;
}

function createReservationRow(reservation) {
  const row = document.createElement("tr");
  row.append(createCell(reservation.name));
  row.append(createCell(reservation.date));
  row.append(createCell(formatStartAt(reservation.time.startAt)));
  row.append(createCell(reservation.theme.name));

  const changeCell = createActionCell(
    createSecondaryButton("변경", () => openChangeModal(reservation))
  );
  const cancelCell = createActionCell(
    createDangerButton("취소", () => confirmReservationDelete(reservation))
  );
  row.append(changeCell);
  row.append(cancelCell);
  return row;
}

function resetReservationFilters() {
  adminElements.reservationFilterName.value = "";
  adminElements.reservationFilterDate.value = "";
  adminElements.reservationFilterTheme.value = "";
  adminState.reservationPage = 1;
  renderReservations();
}

async function confirmReservationDelete(reservation) {
  const message = `[${reservation.name} / ${reservation.date} / ${formatStartAt(reservation.time.startAt)} / ${reservation.theme.name}] 예약을 취소하시겠습니까?`;
  const confirmed = await showConfirmModal(message);
  if (!confirmed) return;
  const ok = await request(`/reservations/${reservation.id}`, {method: "DELETE"});
  if (!ok) return;
  showToast("예약이 취소되었습니다.");
  await loadAdminData();
}

// ── 예약 변경 모달 ────────────────────────────────────────

function populateChangeThemeSelect() {
  const currentValue = adminElements.changeThemeSelect.value;
  adminElements.changeThemeSelect.innerHTML = "";
  adminState.themes.forEach((theme) => {
    const option = document.createElement("option");
    option.value = String(theme.id);
    option.textContent = theme.name;
    adminElements.changeThemeSelect.appendChild(option);
  });
  if (currentValue) adminElements.changeThemeSelect.value = currentValue;
}

async function openChangeModal(reservation) {
  adminState.changingReservation = reservation;
  adminState.changeSelectedTimeId = null;

  adminElements.changeThemeSelect.value = String(reservation.theme.id);
  adminElements.changeDateInput.value = reservation.date;
  adminElements.changeModalConfirm.disabled = true;
  adminElements.changeModal.hidden = false;
  adminElements.changeDateInput.focus();

  await loadChangeAvailableTimes();
}

function closeChangeModal() {
  adminElements.changeModal.hidden = true;
  adminState.changingReservation = null;
  adminState.changeSelectedTimeId = null;
}

async function handleChangeThemeChange() {
  adminState.changeSelectedTimeId = null;
  adminElements.changeModalConfirm.disabled = true;
  await loadChangeAvailableTimes();
}

async function handleChangeDateChange() {
  adminState.changeSelectedTimeId = null;
  adminElements.changeModalConfirm.disabled = true;
  await loadChangeAvailableTimes();
}

async function loadChangeAvailableTimes() {
  const themeId = adminElements.changeThemeSelect.value;
  const date = adminElements.changeDateInput.value;
  if (!themeId || !date) return;

  renderLoading(adminElements.changeTimeList, "시간을 불러오는 중입니다.");
  const times = await requestJson(`/times/available?date=${date}&themeId=${themeId}`);
  if (!times) return;
  renderChangeTimeList(times);
}

function renderChangeTimeList(times) {
  adminElements.changeTimeList.innerHTML = "";
  if (times.length === 0) {
    renderEmpty(adminElements.changeTimeList, "선택 가능한 시간이 없습니다.");
    return;
  }
  times.forEach((t) => adminElements.changeTimeList.appendChild(createChangeTimeButton(t)));
}

function createChangeTimeButton(timeAvailability) {
  const button = document.createElement("button");
  button.type = "button";
  button.dataset.timeId = timeAvailability.time.id;
  button.className = changeTimeButtonClassName(timeAvailability.time.id);
  button.disabled = !timeAvailability.available;
  button.textContent = formatStartAt(timeAvailability.time.startAt);
  button.addEventListener("click", () => handleChangeTimeSelect(timeAvailability.time.id));
  return button;
}

function changeTimeButtonClassName(timeId) {
  return timeId === adminState.changeSelectedTimeId ? "time-button is-selected" : "time-button";
}

function handleChangeTimeSelect(timeId) {
  adminState.changeSelectedTimeId = timeId;
  adminElements.changeModalConfirm.disabled = false;
  Array.from(adminElements.changeTimeList.querySelectorAll(".time-button")).forEach((btn) => {
    btn.className = Number(btn.dataset.timeId) === timeId ? "time-button is-selected" : "time-button";
  });
}

async function handleChangeConfirm() {
  const reservation = adminState.changingReservation;
  if (!reservation || !adminState.changeSelectedTimeId) return;

  const payload = {
    date: adminElements.changeDateInput.value,
    timeId: adminState.changeSelectedTimeId,
  };

  const ok = await request(`/reservations/${reservation.id}`, {
    method: "PATCH",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(payload),
  });

  if (!ok) return;
  closeChangeModal();
  showToast("예약이 변경되었습니다.");
  await loadAdminData();
}

// ── 테마 관리 ────────────────────────────────────────────

async function handleThemeSubmit(event) {
  event.preventDefault();
  const result = await requestJson("/admin/themes", createPostOption(themePayload()));
  if (!result) return;
  adminElements.themeForm.reset();
  showToast("테마가 추가되었습니다.");
  await loadAdminData();
}

function themePayload() {
  return {
    name: adminElements.themeName.value.trim(),
    description: adminElements.themeDescription.value.trim(),
    thumbnailUrl: adminElements.themeThumbnail.value.trim(),
  };
}

function renderThemes() {
  adminElements.themeAdminList.innerHTML = "";
  if (adminState.themes.length === 0) {
    renderEmpty(adminElements.themeAdminList, "등록된 테마가 없습니다.");
    return;
  }
  adminState.themes.forEach((theme) => {
    adminElements.themeAdminList.appendChild(createThemeAdminItem(theme));
  });
}

function createThemeAdminItem(theme) {
  const item = createAdminItem();
  item.append(createThemeAdminContent(theme));
  item.append(createStatusBadge(themeReservationCount(theme.id)));
  item.append(createDangerButton("삭제", () => confirmThemeDelete(theme)));
  return item;
}

function createThemeAdminContent(theme) {
  const content = document.createElement("div");
  content.className = "admin-item-content";
  content.append(createTextElement("strong", theme.name));
  content.append(createTextElement("span", theme.description));
  return content;
}

async function confirmThemeDelete(theme) {
  const count = themeReservationCount(theme.id);
  const confirmed = await showConfirmModal(deleteConfirmMessage(theme.name, count));
  if (!confirmed) return;
  const ok = await request(`/admin/themes/${theme.id}`, {method: "DELETE"});
  if (!ok) return;
  showToast("테마가 삭제되었습니다.");
  await loadAdminData();
}

// ── 시간 관리 ────────────────────────────────────────────

async function handleTimeSubmit(event) {
  event.preventDefault();
  const result = await requestJson("/admin/times", createPostOption(timePayload()));
  if (!result) return;
  adminElements.timeForm.reset();
  showToast("예약 시간이 추가되었습니다.");
  await loadAdminData();
}

function timePayload() {
  return {startAt: adminElements.timeStartAt.value};
}

function renderTimes() {
  adminElements.timeAdminList.innerHTML = "";
  if (adminState.times.length === 0) {
    renderEmpty(adminElements.timeAdminList, "등록된 예약 시간이 없습니다.");
    return;
  }
  adminState.times.forEach((time) => {
    adminElements.timeAdminList.appendChild(createTimeAdminItem(time));
  });
}

function createTimeAdminItem(time) {
  const item = createAdminItem();
  item.append(createTimeAdminContent(time));
  item.append(createStatusBadge(timeReservationCount(time.id)));
  item.append(createDangerButton("삭제", () => confirmTimeDelete(time)));
  return item;
}

function createTimeAdminContent(time) {
  const content = document.createElement("div");
  content.className = "admin-item-content";
  content.append(createTextElement("strong", formatStartAt(time.startAt)));
  content.append(createTextElement("span", "공통 예약 시간"));
  return content;
}

async function confirmTimeDelete(time) {
  const count = timeReservationCount(time.id);
  const confirmed = await showConfirmModal(deleteConfirmMessage(formatStartAt(time.startAt), count));
  if (!confirmed) return;
  const ok = await request(`/admin/times/${time.id}`, {method: "DELETE"});
  if (!ok) return;
  showToast("예약 시간이 삭제되었습니다.");
  await loadAdminData();
}

function deleteConfirmMessage(name, count) {
  if (count > 0) {
    return `[${name}] 항목은 예약 ${count}건에서 사용 중입니다. 삭제하시겠습니까?`;
  }
  return `[${name}] 항목을 삭제하시겠습니까?`;
}

// ── 공통 유틸 ─────────────────────────────────────────────

function themeReservationCount(themeId) {
  return adminState.reservations.filter((r) => r.theme.id === themeId).length;
}

function timeReservationCount(timeId) {
  return adminState.reservations.filter((r) => r.time.id === timeId).length;
}

function createStatusBadge(count) {
  const badge = document.createElement("span");
  badge.className = count > 0 ? "status-badge is-used" : "status-badge";
  badge.textContent = count > 0 ? `예약 ${count}건` : "사용 가능";
  return badge;
}

function createAdminItem() {
  const item = document.createElement("article");
  item.className = "admin-item";
  return item;
}

function createDangerButton(text, clickHandler) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "danger-button";
  button.textContent = text;
  button.addEventListener("click", clickHandler);
  return button;
}

function createSecondaryButton(text, clickHandler) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "secondary-button";
  button.textContent = text;
  button.addEventListener("click", clickHandler);
  return button;
}

function createCell(text) {
  const cell = document.createElement("td");
  cell.textContent = text;
  return cell;
}

function createActionCell(button) {
  const cell = document.createElement("td");
  cell.appendChild(button);
  return cell;
}

function renderTableEmpty(tableBody, columnCount, message) {
  const row = document.createElement("tr");
  const cell = document.createElement("td");
  cell.colSpan = columnCount;
  cell.appendChild(createTextElement("div", message));
  cell.firstElementChild.className = "empty";
  row.appendChild(cell);
  tableBody.appendChild(row);
}

function createPostOption(payload) {
  return {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(payload),
  };
}

async function requestJson(url, options = {}) {
  const response = await request(url, options);
  if (!response) return null;
  return response.json();
}

async function request(url, options = {}) {
  const response = await fetch(url, options);
  if (response.ok) return response;
  await handleErrorResponse(response);
  return null;
}

async function handleErrorResponse(response) {
  const error = await readError(response);
  showErrorModal(error.message);
}

async function readError(response) {
  const fallback = {message: "요청을 처리하지 못했습니다."};
  try {
    return await response.json();
  } catch {
    return fallback;
  }
}

function renderEmpty(container, message) {
  container.innerHTML = `<div class="empty">${message}</div>`;
}

function renderLoading(container, message) {
  container.innerHTML = `<div class="loading">${message}</div>`;
}

function showToast(message) {
  adminElements.toast.textContent = message;
  adminElements.toast.className = "toast";
  adminElements.toast.hidden = false;
  window.setTimeout(() => {
    adminElements.toast.hidden = true;
  }, 2400);
}

function formatStartAt(startAt) {
  return startAt.slice(0, 5);
}

function createTextElement(tagName, text) {
  const element = document.createElement(tagName);
  element.textContent = text;
  return element;
}
