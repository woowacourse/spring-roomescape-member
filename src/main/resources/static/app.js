/**
 * 사용자 예약 페이지 로직 (templates/index.html)
 *
 * 백엔드 API
 *  - GET    /themes                                  : 테마 목록
 *  - GET    /themes/popular                          : 최근 7일 인기 테마
 *  - GET    /themes/{themeId}/available-times?date=  : 특정 테마/날짜의 예약 가능 시간
 *  - POST   /reservations                            : 예약 생성
 */
const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

const state = {
  themes: [],
  selectedThemeId: null,
  availableTimes: []
};

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options
  });

  if (!response.ok) {
    let message;
    try {
      const body = await response.json();
      message = body.message || JSON.stringify(body);
    } catch (_) {
      message = (await response.text()) || `요청 실패 (HTTP ${response.status})`;
    }
    throw new Error(message);
  }

  if (response.status === 204) return null;
  return response.json();
}

function setMessage(message, isError = false) {
  const el = $("#message");
  el.textContent = message;
  el.classList.toggle("error", isError);
}

function setSuccess(message) {
  const box = $("#reservationSuccess");
  box.textContent = message;
  box.classList.add("visible");
}

function clearSuccess() {
  const box = $("#reservationSuccess");
  box.textContent = "";
  box.classList.remove("visible");
}

/* ───────── Theme grid ───────── */

function renderThemes() {
  const root = $("#themeGrid");
  root.innerHTML = "";

  if (state.themes.length === 0) {
    root.innerHTML = '<p class="chip-empty">등록된 테마가 없습니다.</p>';
    return;
  }

  state.themes.forEach((theme) => {
    const card = document.createElement("button");
    card.type = "button";
    card.className = "theme-card";
    card.dataset.themeId = theme.id;
    if (theme.id === state.selectedThemeId) card.classList.add("selected");

    card.innerHTML = `
      <div class="thumb" style="background-image: url('${escapeAttr(theme.thumbnailUrl)}');"></div>
      <div class="body">
        <div class="name">${escapeHtml(theme.name)}</div>
        <div class="desc">${escapeHtml(theme.description)}</div>
      </div>
    `;
    root.appendChild(card);
  });
}

function selectTheme(themeId) {
  state.selectedThemeId = themeId;
  $$("#themeGrid .theme-card").forEach((card) => {
    card.classList.toggle("selected", Number(card.dataset.themeId) === themeId);
  });
  // 선택이 바뀌면 이전 가용 시간 표시는 무효화
  state.availableTimes = [];
  renderAvailableTimes();
  clearSuccess();
}

/* ───────── Available times ───────── */

function renderAvailableTimes() {
  const root = $("#availableTimes");
  root.innerHTML = "";

  if (!state.selectedThemeId) {
    root.innerHTML = '<p class="chip-empty">먼저 테마를 선택해 줘.</p>';
    return;
  }
  if (state.availableTimes.length === 0) {
    root.innerHTML = '<p class="chip-empty">날짜 입력 후 [예약 가능 시간 조회]를 눌러 줘.</p>';
    return;
  }

  state.availableTimes.forEach((time) => {
    const button = document.createElement("button");
    button.className = "chip";
    button.type = "button";
    button.dataset.timeId = time.id;
    button.dataset.startAt = time.startAt;
    button.textContent = `${time.startAt} 예약하기`;
    root.appendChild(button);
  });
}

async function loadAvailableTimes() {
  const date = $('input[name="date"]').value;
  const themeId = state.selectedThemeId;

  if (!themeId) {
    setMessage("먼저 테마를 선택해 주세요.", true);
    return;
  }
  if (!date) {
    setMessage("날짜를 입력해 주세요.", true);
    return;
  }

  state.availableTimes = await api(`/themes/${themeId}/available-times?date=${date}`);
  renderAvailableTimes();
}

/* ───────── Popular themes ───────── */

function renderPopularThemes(popularThemes) {
  const list = $("#popularThemes");
  list.innerHTML = "";

  if (popularThemes.length === 0) {
    const li = document.createElement("li");
    li.innerHTML = '<span class="rank-name">최근 7일 동안 예약된 테마가 없습니다.</span>';
    list.appendChild(li);
    return;
  }

  popularThemes.forEach((theme) => {
    const li = document.createElement("li");
    li.innerHTML = `
      <span class="rank-name">${escapeHtml(theme.name)}</span>
      <span class="rank-desc"> · ${escapeHtml(theme.description)}</span>
    `;
    list.appendChild(li);
  });
}

async function loadThemes() {
  state.themes = await api("/themes");
  renderThemes();
}

async function loadPopularThemes() {
  const popular = await api("/themes/popular");
  renderPopularThemes(popular);
}

/* ───────── Helpers ───────── */

function escapeHtml(str) {
  return String(str ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function escapeAttr(str) {
  return escapeHtml(str);
}

/* ───────── Event wiring ───────── */

$("#themeGrid").addEventListener("click", (event) => {
  const card = event.target.closest(".theme-card[data-theme-id]");
  if (!card) return;
  selectTheme(Number(card.dataset.themeId));
  setMessage(`테마 #${card.dataset.themeId} 를 선택했습니다.`);
});

$("#loadTimes").addEventListener("click", async () => {
  try {
    await loadAvailableTimes();
    if (state.availableTimes.length === 0) {
      setMessage("해당 날짜/테마에 예약 가능한 시간이 없습니다.");
    } else {
      setMessage(`${state.availableTimes.length}개의 시간을 조회했습니다.`);
    }
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#availableTimes").addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-time-id]");
  if (!button) return;

  const name = $('input[name="name"]').value.trim();
  const date = $('input[name="date"]').value;
  const themeId = state.selectedThemeId;

  if (!name) {
    setMessage("예약자 이름을 입력해 주세요.", true);
    return;
  }
  if (!date || !themeId) {
    setMessage("날짜와 테마를 모두 선택해 주세요.", true);
    return;
  }

  try {
    const created = await api("/reservations", {
      method: "POST",
      body: JSON.stringify({
        name,
        date,
        timeId: Number(button.dataset.timeId),
        themeId: Number(themeId)
      })
    });

    setSuccess(
      `예약 성공 · #${created.id} / [${created.theme?.name ?? ""}] ${created.date} ${created.time?.startAt ?? ""} / ${created.name}`
    );
    setMessage("예약이 정상적으로 완료되었습니다.");

    await loadAvailableTimes();
    await loadPopularThemes();
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#loadPopular").addEventListener("click", async () => {
  try {
    await loadPopularThemes();
    setMessage("인기 테마를 갱신했습니다.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

/* ───────── Init ───────── */

(async function init() {
  // 날짜 기본값 = 오늘
  const today = new Date().toISOString().slice(0, 10);
  $('input[name="date"]').value = today;

  try {
    await Promise.all([loadThemes(), loadPopularThemes()]);
    setMessage("초기 데이터 로딩 완료. 테마를 선택해 주세요.");
  } catch (error) {
    setMessage(error.message, true);
  }
})();
