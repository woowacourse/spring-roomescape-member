const API_BASE = new URLSearchParams(window.location.search).get("apiBase") || "http://localhost:8080";
const statusEl = document.getElementById("status");
const confirmModalEl = document.getElementById("confirm-modal");
const confirmTitleEl = document.getElementById("confirm-title");
const confirmMessageEl = document.getElementById("confirm-message");
const confirmOkEl = document.getElementById("confirm-ok");
const confirmCancelEl = document.getElementById("confirm-cancel");
const resultModalEl = document.getElementById("result-modal");
const resultTitleEl = document.getElementById("result-title");
const resultMessageEl = document.getElementById("result-message");
const resultOkEl = document.getElementById("result-ok");

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
  if (!response.ok) throw new Error(`요청 실패: ${response.status}`);
  if (response.status === 204) return null;
  return response.json();
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

function renderThemeList(themes) {
  const list = document.getElementById("theme-list");
  list.innerHTML = "";
  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.innerHTML = `<strong>#${theme.id} ${theme.name}</strong> - ${theme.description}`;
    list.appendChild(li);
  });
}

function renderReservationList(reservations) {
  const list = document.getElementById("reservation-list");
  list.innerHTML = "";
  reservations.forEach((reservation) => {
    const li = document.createElement("li");
    li.innerHTML = `#${reservation.id} ${reservation.name} / ${reservation.date} / ${reservation.time.time} / themeId=${reservation.themeId}`;
    list.appendChild(li);
  });
}

function renderTimeList(times) {
  const list = document.getElementById("time-list");
  list.innerHTML = "";
  times.forEach((time) => {
    const li = document.createElement("li");
    li.innerHTML = `#${time.id} ${time.startAt}`;
    list.appendChild(li);
  });
}

async function loadReservations() {
  const reservations = await api("/reservations");
  renderReservationList(reservations);
}

async function loadTimes() {
  const times = await api("/times");
  renderTimeList(times);
}

document.getElementById("theme-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const name = document.getElementById("theme-name").value;
    const description = document.getElementById("theme-description").value;
    const thumbnailUrl = document.getElementById("theme-thumbnail").value;
    const confirmed = await confirmAction({
      title: "테마 추가 확인",
      message: `${name} 테마를 추가할까요?`,
      okLabel: "추가",
    });
    if (!confirmed) {
      setStatus("테마 추가 취소");
      return;
    }

    await api("/themes", {
      method: "POST",
      body: JSON.stringify({
        name,
        description,
        thumbnailUrl,
      }),
    });
    setStatus("테마 추가 완료");
    e.target.reset();
    await showResultModal({
      title: "테마 추가 성공",
      message: `${name} 테마가 추가되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "테마 추가 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("theme-refresh").addEventListener("click", async () => {
  try {
    const themes = await api("/themes");
    renderThemeList(themes);
    setStatus("테마 목록 조회 완료");
  } catch (error) {
    setStatus(error.message, true);
  }
});

document.getElementById("theme-delete-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById("theme-delete-id").value;
    const confirmed = await confirmAction({
      title: "테마 삭제 확인",
      message: `테마 ID ${id}를 삭제할까요?`,
      okLabel: "삭제",
      okDanger: true,
    });
    if (!confirmed) {
      setStatus("테마 삭제 취소");
      return;
    }

    await api(`/themes/${id}`, { method: "DELETE" });
    setStatus(`테마 #${id} 삭제 완료`);
    e.target.reset();
    await showResultModal({
      title: "테마 삭제 성공",
      message: `테마 ID ${id}가 삭제되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "테마 삭제 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("reservation-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const name = document.getElementById("reservation-name").value;
    const date = document.getElementById("reservation-date").value;
    const timeId = Number(document.getElementById("reservation-time-id").value);
    const themeId = Number(document.getElementById("reservation-theme-id").value);
    const confirmed = await confirmAction({
      title: "예약 추가 확인",
      message: `${name} / ${date} / timeId ${timeId} / themeId ${themeId} 예약을 추가할까요?`,
      okLabel: "추가",
    });
    if (!confirmed) {
      setStatus("예약 추가 취소");
      return;
    }

    await api("/reservations", {
      method: "POST",
      body: JSON.stringify({
        name,
        date,
        timeId,
        themeId,
      }),
    });
    await loadReservations();
    setStatus("예약 추가 완료");
    await showResultModal({
      title: "예약 추가 성공",
      message: `예약자 ${name}님의 예약이 추가되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "예약 추가 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("reservation-delete-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById("reservation-delete-id").value;
    const confirmed = await confirmAction({
      title: "예약 삭제 확인",
      message: `예약 ID ${id}를 삭제할까요?`,
      okLabel: "삭제",
      okDanger: true,
    });
    if (!confirmed) {
      setStatus("예약 삭제 취소");
      return;
    }

    await api(`/reservations/${id}`, { method: "DELETE" });
    await loadReservations();
    setStatus(`예약 #${id} 삭제 완료`);
    e.target.reset();
    await showResultModal({
      title: "예약 삭제 성공",
      message: `예약 ID ${id}가 삭제되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "예약 삭제 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("reservation-refresh").addEventListener("click", async () => {
  try {
    await loadReservations();
    setStatus("예약 목록 조회 완료");
  } catch (error) {
    setStatus(error.message, true);
  }
});

document.getElementById("time-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const startAt = document.getElementById("time-start-at").value;
    const confirmed = await confirmAction({
      title: "시간 슬롯 추가 확인",
      message: `${startAt} 시간 슬롯을 추가할까요?`,
      okLabel: "추가",
    });
    if (!confirmed) {
      setStatus("시간 슬롯 추가 취소");
      return;
    }

    await api("/times", {
      method: "POST",
      body: JSON.stringify({
        startAt,
      }),
    });
    await loadTimes();
    setStatus("시간 슬롯 추가 완료");
    e.target.reset();
    await showResultModal({
      title: "시간 슬롯 추가 성공",
      message: `${startAt} 시간 슬롯이 추가되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "시간 슬롯 추가 실패",
      message: error.message,
      isError: true,
    });
  }
});

document.getElementById("time-refresh").addEventListener("click", async () => {
  try {
    await loadTimes();
    setStatus("시간 슬롯 조회 완료");
  } catch (error) {
    setStatus(error.message, true);
  }
});

document.getElementById("time-delete-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const id = document.getElementById("time-delete-id").value;
    const confirmed = await confirmAction({
      title: "시간 슬롯 삭제 확인",
      message: `시간 슬롯 ID ${id}를 삭제할까요?`,
      okLabel: "삭제",
      okDanger: true,
    });
    if (!confirmed) {
      setStatus("시간 슬롯 삭제 취소");
      return;
    }

    await api(`/times/${id}`, { method: "DELETE" });
    await loadTimes();
    setStatus(`시간 슬롯 #${id} 삭제 완료`);
    e.target.reset();
    await showResultModal({
      title: "시간 슬롯 삭제 성공",
      message: `시간 슬롯 ID ${id}가 삭제되었습니다.`,
    });
  } catch (error) {
    setStatus(error.message, true);
    await showResultModal({
      title: "시간 슬롯 삭제 실패",
      message: error.message,
      isError: true,
    });
  }
});

function setTodayDefault() {
  const today = new Date().toISOString().slice(0, 10);
  document.getElementById("reservation-date").value = today;
}

setTodayDefault();
