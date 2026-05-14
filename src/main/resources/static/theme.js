/**
 * 테마 상세 예약 페이지 (static/theme.html)
 *
 * API
 *  GET  /themes/{id}                           : 테마 정보
 *  GET  /themes/{id}/available-times?date=     : 예약 가능 시간
 *  POST /reservations                          : 예약 생성
 */
const $ = (sel) => document.querySelector(sel);

const themeId = Number(new URLSearchParams(location.search).get('id'));

/* ── API ── */
async function api(path, options = {}) {
  const res = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    let msg;
    try { const b = await res.json(); msg = b.message || JSON.stringify(b); }
    catch (_) { msg = `요청 실패 (HTTP ${res.status})`; }
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  return res.json();
}

function escapeHtml(str) {
  return String(str ?? "")
    .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}

function setMessage(msg, isError = false) {
  const el = $("#message");
  el.textContent = msg;
  el.classList.toggle("error", isError);
}

/* ── Theme hero ── */
async function loadTheme() {
  const theme = await api(`/themes/${themeId}`);
  document.title = `${theme.name} · Roomescape`;

  const hero = $("#themeHero");
  if (theme.thumbnailUrl) {
    const img = document.createElement("img");
    img.className = "theme-hero-img";
    img.src = theme.thumbnailUrl;
    img.alt = theme.name;
    hero.prepend(img);
  }

  $("#themeInfo").innerHTML = `
    <p class="eyebrow">// 테마</p>
    <h1>${escapeHtml(theme.name)}</h1>
    <p>${escapeHtml(theme.description)}</p>
  `;
}

/* ── Calendar ── */
const calState = {
  year: new Date().getFullYear(),
  month: new Date().getMonth(), // 0-indexed
  selectedDate: null,
};

const DAYS_KO = ["일", "월", "화", "수", "목", "금", "토"];
const MONTHS_KO = ["1월", "2월", "3월", "4월", "5월", "6월",
                   "7월", "8월", "9월", "10월", "11월", "12월"];

function renderCalendar() {
  const { year, month, selectedDate } = calState;
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  $("#calTitle").textContent = `${year}년 ${MONTHS_KO[month]}`;

  const grid = $("#calGrid");
  grid.innerHTML = "";

  // 요일 헤더
  DAYS_KO.forEach((d) => {
    const cell = document.createElement("div");
    cell.className = "cal-dow";
    cell.textContent = d;
    grid.appendChild(cell);
  });

  const firstDay = new Date(year, month, 1).getDay(); // 0=일
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  // 빈 칸 (월 시작 전)
  for (let i = 0; i < firstDay; i++) {
    const empty = document.createElement("div");
    empty.className = "cal-cell cal-empty";
    grid.appendChild(empty);
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const cellDate = new Date(year, month, d);
    cellDate.setHours(0, 0, 0, 0);
    const isPast = cellDate < today;
    const isToday = cellDate.getTime() === today.getTime();
    const dateStr = `${year}-${String(month + 1).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
    const isSelected = selectedDate === dateStr;

    const cell = document.createElement("div");
    cell.className = "cal-cell"
      + (isPast ? " cal-past" : "")
      + (isToday ? " cal-today" : "")
      + (isSelected ? " cal-selected" : "");
    cell.textContent = d;
    if (!isPast) cell.dataset.date = dateStr;
    grid.appendChild(cell);
  }
}

function selectDate(dateStr) {
  calState.selectedDate = dateStr;
  renderCalendar();
  $("#selectedDateLabel").textContent = dateStr;
  loadAvailableTimes(dateStr);
}

/* ── Available times ── */
async function loadAvailableTimes(date) {
  const root = $("#availableTimes");
  root.innerHTML = '<p class="chip-empty">불러오는 중…</p>';
  try {
    const times = await api(`/themes/${themeId}/available-times?date=${date}`);
    renderTimes(times, date);
  } catch (e) {
    root.innerHTML = `<p class="chip-empty" style="color:var(--danger);">${escapeHtml(e.message)}</p>`;
  }
}

function renderTimes(times, date) {
  const root = $("#availableTimes");
  root.innerHTML = "";
  if (!times.length) {
    root.innerHTML = '<p class="chip-empty">해당 날짜에 예약 가능한 시간이 없습니다.</p>';
    return;
  }
  times.forEach((t) => {
    const btn = document.createElement("button");
    btn.className = "chip";
    btn.type = "button";
    btn.dataset.timeId = t.id;
    btn.dataset.date = date;
    btn.textContent = `${t.startAt} 예약`;
    root.appendChild(btn);
  });
}

/* ── Booking ── */
$("#availableTimes").addEventListener("click", async (e) => {
  const btn = e.target.closest("button[data-time-id]");
  if (!btn) return;

  const name = $("#bookingName").value.trim();
  if (!name) {
    setMessage("예약자 이름을 입력해 주세요.", true);
    $("#bookingName").focus();
    return;
  }

  const timeId = Number(btn.dataset.timeId);
  const date = btn.dataset.date;

  btn.disabled = true;
  try {
    const created = await api("/reservations", {
      method: "POST",
      body: JSON.stringify({ name, date, timeId, themeId }),
    });

    const box = $("#successBox");
    box.textContent = `✓ 예약 완료 · #${created.id} / ${created.date} ${created.time?.startAt ?? ""} / ${created.name}`;
    box.classList.add("visible");
    setMessage("");

    // 해당 날짜 시간 목록 갱신
    await loadAvailableTimes(date);
  } catch (e) {
    setMessage(e.message, true);
    btn.disabled = false;
  }
});

/* ── Calendar navigation ── */
$("#calPrev").addEventListener("click", () => {
  calState.month--;
  if (calState.month < 0) { calState.month = 11; calState.year--; }
  renderCalendar();
});
$("#calNext").addEventListener("click", () => {
  calState.month++;
  if (calState.month > 11) { calState.month = 0; calState.year++; }
  renderCalendar();
});
$("#calGrid").addEventListener("click", (e) => {
  const cell = e.target.closest(".cal-cell[data-date]");
  if (!cell) return;
  selectDate(cell.dataset.date);
});

/* ── Init ── */
(async function init() {
  renderCalendar();
  try {
    await loadTheme();
  } catch (e) {
    setMessage(e.message, true);
  }
})();
