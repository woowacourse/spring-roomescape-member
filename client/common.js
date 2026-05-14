const API_BASE = new URLSearchParams(window.location.search).get("apiBase") || "http://localhost:8080";
const statusEl = document.getElementById("status");
const reservationSubmitEl = document.getElementById("reservation-submit");
const selectedThemeLabelEl = document.getElementById("selected-theme-label");
const selectedTimeLabelEl = document.getElementById("selected-time-label");
const resultModalEl = document.getElementById("result-modal");
const resultTitleEl = document.getElementById("result-title");
const resultMessageEl = document.getElementById("result-message");
const resultOkEl = document.getElementById("result-ok");
const confirmModalEl = document.getElementById("confirm-modal");
const confirmTitleEl = document.getElementById("confirm-title");
const confirmMessageEl = document.getElementById("confirm-message");
const confirmOkEl = document.getElementById("confirm-ok");
const confirmCancelEl = document.getElementById("confirm-cancel");
const myInfoOpenEl = document.getElementById("my-info-open");
const myInfoModalEl = document.getElementById("my-info-modal");
const myInfoFormEl = document.getElementById("my-info-form");
const myInfoNameEl = document.getElementById("my-info-name");
const myInfoCancelEl = document.getElementById("my-info-cancel");
const myReservationOwnerEl = document.getElementById("my-reservation-owner");
const myReservationCardsEl = document.getElementById("my-reservation-cards");
const editReservationModalEl = document.getElementById("edit-reservation-modal");
const editReservationFormEl = document.getElementById("edit-reservation-form");
const editReservationIdEl = document.getElementById("edit-reservation-id");
const editReservationDateEl = document.getElementById("edit-reservation-date");
const editReservationThemeEl = document.getElementById("edit-reservation-theme");
const editAvailableTimesEl = document.getElementById("edit-available-times");
const editReservationCancelEl = document.getElementById("edit-reservation-cancel");
let selectedThemeId = null;
let selectedTimeId = null;
let selectedTimeLabel = null;
let selectedThemeName = null;
let myReservationName = null;
let editingReservation = null;
let selectedEditTimeId = null;

function setStatus(message, isError = false) {
  if (!statusEl) return;
  statusEl.textContent = message;
  statusEl.style.color = isError ? "#dc2626" : "#065f46";
}

async function api(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (response.status === 204) return null;
  const body = await response.json();
  if (!response.ok || body.success === false) {
    const message = body?.error?.message || `요청 실패: ${response.status}`;
    throw new Error(message);
  }
  return body.data;
}

function showResultModal({ title, message, isError = false }) {
  return new Promise((resolve) => {
    const card = resultModalEl.querySelector(".modal-card");
    card.classList.remove("success", "error");
    card.classList.add(isError ? "error" : "success");
    resultTitleEl.textContent = title;
    resultMessageEl.textContent = message;
    resultModalEl.classList.remove("hidden");
    resultOkEl.onclick = () => {
      resultModalEl.classList.add("hidden");
      resultOkEl.onclick = null;
      resolve();
    };
  });
}

function confirmAction({ title, message, okLabel = "확인", okDanger = false }) {
  return new Promise((resolve) => {
    confirmTitleEl.textContent = title;
    confirmMessageEl.textContent = message;
    confirmOkEl.textContent = okLabel;
    confirmOkEl.classList.toggle("danger-button", okDanger);
    confirmModalEl.classList.remove("hidden");

    const cleanup = () => {
      confirmModalEl.classList.add("hidden");
      confirmOkEl.onclick = null;
      confirmCancelEl.onclick = null;
    };

    confirmOkEl.onclick = () => {
      cleanup();
      resolve(true);
    };
    confirmCancelEl.onclick = () => {
      cleanup();
      resolve(false);
    };
  });
}

async function showErrorModal(title, error) {
  const message = error?.message || "알 수 없는 오류가 발생했습니다.";
  setStatus(message, true);
  await showResultModal({
    title,
    message,
    isError: true,
  });
}

function renderThemeList(themes) {
  const list = document.getElementById("theme-list");
  list.innerHTML = "";
  if (!themes.length) {
    const li = document.createElement("li");
    li.textContent = "선택한 날짜에 예약 가능한 테마가 없습니다.";
    li.className = "empty-message";
    list.appendChild(li);
    return;
  }

  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.classList.add("theme-item");
    li.dataset.id = String(theme.id);
    li.dataset.name = theme.name;
    li.tabIndex = 0;
    li.setAttribute("role", "button");
    li.innerHTML = `
      <img class="theme-thumbnail" src="${theme.thumbnailUrl}" alt="${theme.name} 썸네일">
      <button class="theme-select" data-id="${theme.id}" type="button">${theme.name}</button>
      - ${theme.description}
      <span class="muted">(ID: ${theme.id})</span>
    `;
    list.appendChild(li);
  });
}

function renderAvailableTimes(items) {
  const wrapper = document.getElementById("available-times");
  wrapper.innerHTML = "";
  selectedTimeId = null;
  selectedTimeLabel = null;
  selectedTimeLabelEl.textContent = "시간: 미선택";
  reservationSubmitEl.disabled = true;
  if (!items.length) {
    const empty = document.createElement("span");
    empty.className = "empty-message";
    empty.textContent = "조회된 예약 가능 시간이 없습니다.";
    wrapper.appendChild(empty);
    return;
  }

  items.forEach((item) => {
    const button = document.createElement("button");
    button.type = "button";
    button.className = `chip${item.isAvailable ? "" : " disabled"}`;
    button.textContent = `${item.timeInformation.time} ${item.isAvailable ? "(가능)" : "(불가)"}`;
    if (!item.isAvailable) {
      button.disabled = true;
      wrapper.appendChild(button);
      return;
    }
    button.addEventListener("click", () => {
      document.querySelectorAll("#available-times .chip").forEach((chip) => chip.classList.remove("selected"));
      button.classList.add("selected");
      selectedTimeId = item.timeInformation.id;
      selectedTimeLabel = item.timeInformation.time;
      selectedTimeLabelEl.textContent = `시간: ${selectedTimeLabel}`;
      reservationSubmitEl.disabled = false;
      setStatus(`시간 ${selectedTimeLabel} 선택됨`);
    });
    wrapper.appendChild(button);
  });
}

function renderPopular(themes) {
  const list = document.getElementById("popular-list");
  list.innerHTML = "";
  if (!themes.length) {
    const li = document.createElement("li");
    li.className = "empty-message";
    li.textContent = "조회된 인기 테마가 없습니다.";
    list.appendChild(li);
    return;
  }
  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.textContent = `${theme.name} - ${theme.description}`;
    list.appendChild(li);
  });
}

function renderMyReservationCards(reservations) {
  myReservationCardsEl.innerHTML = "";
  if (!reservations.length) {
    const empty = document.createElement("p");
    empty.className = "empty-message";
    empty.textContent = "조회된 내 예약이 없습니다.";
    myReservationCardsEl.appendChild(empty);
    return;
  }

  reservations.forEach((reservation) => {
    const card = document.createElement("article");
    card.className = "reservation-card";
    card.innerHTML = `
      <div class="reservation-card-head">
        <div>
          <strong>${reservation.name}</strong>
          <p>#${reservation.id}</p>
        </div>
        <div class="menu-wrapper">
          <button type="button" class="menu-button" data-menu-id="${reservation.id}" aria-label="예약 메뉴">☰</button>
          <div class="menu-panel hidden" id="menu-${reservation.id}">
            <button
              type="button"
              class="menu-item edit"
              data-edit-id="${reservation.id}"
              data-edit-date="${reservation.date}"
              data-edit-time-id="${reservation.time.id}"
              data-edit-time-label="${reservation.time.time}"
              data-edit-theme-id="${reservation.theme.id}"
              data-edit-theme-name="${reservation.theme.name}"
            >수정</button>
            <button type="button" class="menu-item delete" data-delete-id="${reservation.id}">삭제</button>
          </div>
        </div>
      </div>
      <div class="reservation-card-body">
        <p><span>날짜</span><strong>${reservation.date}</strong></p>
        <p><span>테마</span><strong>${reservation.theme.name}</strong></p>
        <p><span>시간</span><strong>${reservation.time.time}</strong></p>
      </div>
    `;
    myReservationCardsEl.appendChild(card);
  });
}

async function loadMyReservations(name) {
  const reservations = await api(`/reservations?name=${encodeURIComponent(name)}`);
  myReservationName = name;
  myReservationOwnerEl.textContent = `${name}님의 예약 목록`;
  renderMyReservationCards(reservations);
}

function closeMenuPanels() {
  document.querySelectorAll(".menu-panel").forEach((panel) => panel.classList.add("hidden"));
}

function renderEditAvailableTimes(items, currentTime) {
  editAvailableTimesEl.innerHTML = "";
  selectedEditTimeId = currentTime.id;

  if (!items.length) {
    const empty = document.createElement("span");
    empty.className = "empty-message";
    empty.textContent = "선택 가능한 시간이 없습니다.";
    editAvailableTimesEl.appendChild(empty);
    return;
  }

  items.forEach((item) => {
    const button = document.createElement("button");
    const isCurrent = item.timeInformation.id === currentTime.id;
    const available = item.isAvailable || isCurrent;
    button.type = "button";
    button.className = `chip${available ? "" : " disabled"}${isCurrent ? " selected" : ""}`;
    button.textContent = `${item.timeInformation.time}`;
    if (!available) {
      button.disabled = true;
      editAvailableTimesEl.appendChild(button);
      return;
    }
    button.addEventListener("click", () => {
      editAvailableTimesEl.querySelectorAll(".chip").forEach((chip) => chip.classList.remove("selected"));
      button.classList.add("selected");
      selectedEditTimeId = item.timeInformation.id;
    });
    editAvailableTimesEl.appendChild(button);
  });
}

async function loadEditTimesByDate(date) {
  if (!editingReservation) return;
  const items = await api(`/times/availability?date=${date}&themeId=${editingReservation.themeId}`);
  renderEditAvailableTimes(items, editingReservation.time);
}

document.getElementById("theme-by-date-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const date = document.getElementById("theme-date").value;
    const themes = await api(`/themes?date=${date}`);
    renderThemeList(themes);
    selectedThemeId = null;
    selectedThemeName = null;
    selectedThemeLabelEl.textContent = "테마: 미선택";
    renderAvailableTimes([]);
    setStatus("날짜별 테마 조회 완료");
  } catch (error) {
    await showErrorModal("날짜별 테마 조회 실패", error);
  }
});

async function selectThemeFromListItem(target) {
  const item = target.closest(".theme-item");
  if (!item) return;
  try {
    const date = document.getElementById("theme-date").value;
    const themeId = item.dataset.id;
    const themeName = item.dataset.name;
    document.querySelectorAll(".theme-item").forEach((li) => li.classList.remove("selected"));
    item.classList.add("selected");
    selectedThemeId = Number(themeId);
    selectedThemeName = themeName;
    selectedThemeLabelEl.textContent = `테마: ${selectedThemeName}`;
    const items = await api(`/times/availability?date=${date}&themeId=${themeId}`);
    renderAvailableTimes(items);
    setStatus(`테마 ID ${themeId} 예약 가능 시간 조회 완료`);
  } catch (error) {
    await showErrorModal("예약 가능 시간 조회 실패", error);
  }
}

document.getElementById("theme-list").addEventListener("click", async (e) => {
  await selectThemeFromListItem(e.target);
});

document.getElementById("theme-list").addEventListener("keydown", async (e) => {
  if (e.key !== "Enter" && e.key !== " ") return;
  e.preventDefault();
  await selectThemeFromListItem(e.target);
});

document.getElementById("reservation-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    if (!selectedThemeId || !selectedTimeId) {
      setStatus("테마와 시간을 먼저 선택하세요.", true);
      await showResultModal({
        title: "예약 실패",
        message: "테마와 시간을 먼저 선택해주세요.",
        isError: true,
      });
      return;
    }
    const name = document.getElementById("reservation-name").value;
    const date = document.getElementById("theme-date").value;
    const confirmed = await confirmAction({
      title: "예약 확인",
      message: `예약자 ${name}\n날짜 ${date}\n테마 ${selectedThemeName}\n시간 ${selectedTimeLabel}\n\n예약하시겠습니까?`,
      okLabel: "예약",
    });
    if (!confirmed) {
      setStatus("예약이 취소되었습니다.");
      return;
    }

    await api("/reservations", {
      method: "POST",
      body: JSON.stringify({
        name,
        date,
        timeId: selectedTimeId,
        themeId: selectedThemeId,
      }),
    });
    setStatus(`예약 완료: ${name} / ${date} / ${selectedTimeLabel}`);
    await showResultModal({
      title: "예약 성공",
      message: `${name}님의 예약이 완료되었습니다.`,
    });
    document.getElementById("reservation-name").value = "";
    const items = await api(`/times/availability?date=${date}&themeId=${selectedThemeId}`);
    renderAvailableTimes(items);
  } catch (error) {
    setStatus(`예약 실패: ${error.message}`, true);
    await showResultModal({
      title: "예약 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("popular-refresh").addEventListener("click", async () => {
  try {
    const themes = await api("/themes/popular");
    renderPopular(themes);
    setStatus("인기 테마 조회 완료");
  } catch (error) {
    await showErrorModal("인기 테마 조회 실패", error);
  }
});

myInfoOpenEl.addEventListener("click", () => {
  myInfoModalEl.classList.remove("hidden");
  myInfoNameEl.focus();
});

myInfoCancelEl.addEventListener("click", () => {
  myInfoModalEl.classList.add("hidden");
});

myInfoFormEl.addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const name = myInfoNameEl.value.trim();
    await loadMyReservations(name);
    myInfoModalEl.classList.add("hidden");
    setStatus(`${name} 예약 조회 완료`);
  } catch (error) {
    await showErrorModal("내 예약 조회 실패", error);
  }
});

myReservationCardsEl.addEventListener("click", async (e) => {
  const menuButton = e.target.closest(".menu-button");
  if (menuButton) {
    const menuId = menuButton.dataset.menuId;
    const panel = document.getElementById(`menu-${menuId}`);
    const isOpen = !panel.classList.contains("hidden");
    closeMenuPanels();
    if (!isOpen) panel.classList.remove("hidden");
    return;
  }

  const editButton = e.target.closest(".menu-item.edit");
  if (editButton) {
    closeMenuPanels();
    editingReservation = {
      id: editButton.dataset.editId,
      date: editButton.dataset.editDate,
      time: {
        id: Number(editButton.dataset.editTimeId),
        label: editButton.dataset.editTimeLabel,
      },
      themeId: Number(editButton.dataset.editThemeId),
      themeName: editButton.dataset.editThemeName,
    };
    editReservationIdEl.value = editButton.dataset.editId;
    editReservationDateEl.value = editButton.dataset.editDate;
    editReservationThemeEl.textContent = `테마: ${editingReservation.themeName}`;
    await loadEditTimesByDate(editReservationDateEl.value);
    editReservationModalEl.classList.remove("hidden");
    return;
  }

  const deleteButton = e.target.closest(".menu-item.delete");
  if (!deleteButton) return;

  closeMenuPanels();
  try {
    if (!myReservationName) throw new Error("예약자 이름 정보가 없습니다. 내정보를 다시 조회해주세요.");
    const id = deleteButton.dataset.deleteId;
    const confirmed = await confirmAction({
      title: "예약 삭제 확인",
      message: `예약 ID ${id}를 정말 삭제하시겠습니까?`,
      okLabel: "삭제",
      okDanger: true,
    });
    if (!confirmed) {
      return;
    }
    await api(`/reservations/${id}?name=${encodeURIComponent(myReservationName)}`, { method: "DELETE" });
    await loadMyReservations(myReservationName);
    setStatus(`예약 #${id} 삭제 완료`);
    await showResultModal({
      title: "예약 삭제 성공",
      message: `예약 ID ${id}가 삭제되었습니다.`,
    });
  } catch (error) {
    await showErrorModal("예약 삭제 실패", error);
  }
});

editReservationCancelEl.addEventListener("click", () => {
  editReservationModalEl.classList.add("hidden");
});

editReservationDateEl.addEventListener("change", async () => {
  try {
    await loadEditTimesByDate(editReservationDateEl.value);
  } catch (error) {
    await showErrorModal("수정 가능 시간 조회 실패", error);
  }
});

editReservationFormEl.addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    if (!myReservationName) throw new Error("예약자 이름 정보가 없습니다. 내정보를 다시 조회해주세요.");
    const id = editReservationIdEl.value;
    if (!selectedEditTimeId) throw new Error("수정할 시간을 선택해주세요.");
    await api(`/reservations/${id}?name=${encodeURIComponent(myReservationName)}`, {
      method: "PATCH",
      body: JSON.stringify({
        date: editReservationDateEl.value,
        timeId: Number(selectedEditTimeId),
      }),
    });
    editReservationModalEl.classList.add("hidden");
    await loadMyReservations(myReservationName);
    setStatus(`예약 #${id} 수정 완료`);
    await showResultModal({
      title: "예약 수정 성공",
      message: `예약 ID ${id}가 수정되었습니다.`,
    });
  } catch (error) {
    await showErrorModal("예약 수정 실패", error);
  }
});

document.addEventListener("click", (e) => {
  if (!e.target.closest(".menu-wrapper")) {
    closeMenuPanels();
  }
});

function setTodayDefault() {
  const today = new Date().toISOString().slice(0, 10);
  document.getElementById("theme-date").value = today;
}

setTodayDefault();
