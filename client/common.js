const API_BASE = new URLSearchParams(window.location.search).get("apiBase") || "http://localhost:8080";
const statusEl = document.getElementById("status");
const reservationSubmitEl = document.getElementById("reservation-submit");
const selectedThemeLabelEl = document.getElementById("selected-theme-label");
const selectedTimeLabelEl = document.getElementById("selected-time-label");
let selectedThemeId = null;
let selectedTimeId = null;
let selectedTimeLabel = null;
let selectedThemeName = null;

function setStatus(message, isError = false) {
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

function renderThemeList(themes) {
  const list = document.getElementById("theme-list");
  list.innerHTML = "";
  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.innerHTML = `
      <button class="theme-select" data-id="${theme.id}">${theme.name}</button>
      - ${theme.description}
      <a href="${theme.thumbnailUrl}" target="_blank" rel="noreferrer">썸네일</a>
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
  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.textContent = `${theme.name} - ${theme.description}`;
    list.appendChild(li);
  });
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
    setStatus(error.message, true);
  }
});

document.getElementById("theme-list").addEventListener("click", async (e) => {
  if (!e.target.classList.contains("theme-select")) return;
  try {
    const date = document.getElementById("theme-date").value;
    const themeId = e.target.dataset.id;
    const themeName = e.target.textContent;
    document.querySelectorAll(".theme-select").forEach((button) => button.classList.remove("selected"));
    e.target.classList.add("selected");
    selectedThemeId = Number(themeId);
    selectedThemeName = themeName;
    selectedThemeLabelEl.textContent = `테마: ${selectedThemeName}`;
    const items = await api(`/times/availability?date=${date}&themeId=${themeId}`);
    renderAvailableTimes(items);
    setStatus(`테마 ID ${themeId} 예약 가능 시간 조회 완료`);
  } catch (error) {
    setStatus(error.message, true);
  }
});

document.getElementById("reservation-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    if (!selectedThemeId || !selectedTimeId) {
      setStatus("테마와 시간을 먼저 선택하세요.", true);
      alert("테마와 시간을 먼저 선택해주세요.");
      return;
    }
    const name = document.getElementById("reservation-name").value;
    const date = document.getElementById("theme-date").value;
    const confirmed = window.confirm(
      `다음 정보로 예약하시겠습니까?\n\n예약자: ${name}\n날짜: ${date}\n테마: ${selectedThemeName}\n시간: ${selectedTimeLabel}`
    );
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
    alert("예약이 완료되었습니다.");
    document.getElementById("reservation-name").value = "";
    const items = await api(`/times/availability?date=${date}&themeId=${selectedThemeId}`);
    renderAvailableTimes(items);
  } catch (error) {
    setStatus(`예약 실패: ${error.message}`, true);
    alert(`예약에 실패했습니다.\n${error.message}`);
  }
});

document.getElementById("popular-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const day = document.getElementById("popular-day").value;
    const limit = document.getElementById("popular-limit").value;
    const themes = await api(`/themes/popular?day=${day}&limit=${limit}`);
    renderPopular(themes);
    setStatus("인기 테마 조회 완료");
  } catch (error) {
    setStatus(error.message, true);
  }
});

function setTodayDefault() {
  const today = new Date().toISOString().slice(0, 10);
  document.getElementById("theme-date").value = today;
}

setTodayDefault();
