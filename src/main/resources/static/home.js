/**
 * 홈 페이지 (static/index.html)
 *
 * API
 *  GET /themes          : 전체 테마 목록
 *  GET /themes/popular  : 인기 테마 (최근 7일)
 */
const $ = (sel) => document.querySelector(sel);

async function api(path) {
  const res = await fetch(path, { headers: { "Content-Type": "application/json" } });
  if (!res.ok) {
    let msg;
    try { const b = await res.json(); msg = b.message || JSON.stringify(b); }
    catch (_) { msg = `요청 실패 (HTTP ${res.status})`; }
    throw new Error(msg);
  }
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

/* ── Theme grid ── */
function renderThemes(themes) {
  const root = $("#themeGrid");
  root.innerHTML = "";

  if (!themes.length) {
    root.innerHTML = '<p class="chip-empty">등록된 테마가 없습니다.</p>';
    return;
  }

  themes.forEach((theme) => {
    const card = document.createElement("a");
    card.className = "theme-card";
    card.href = `/theme.html?id=${theme.id}`;
    card.innerHTML = `
      <div class="thumb" style="background-image: url('${escapeHtml(theme.thumbnailUrl)}');"></div>
      <div class="body">
        <div class="name">${escapeHtml(theme.name)}</div>
        <div class="desc">${escapeHtml(theme.description)}</div>
      </div>
    `;
    root.appendChild(card);
  });
}

/* ── Popular themes ── */
function renderPopular(themes) {
  const list = $("#popularThemes");
  list.innerHTML = "";

  if (!themes.length) {
    list.innerHTML = '<li><span class="rank-name muted">최근 7일 예약이 없습니다.</span></li>';
    return;
  }

  themes.forEach((theme) => {
    const li = document.createElement("li");
    li.innerHTML = `
      <span class="rank-name">
        <a href="/theme.html?id=${theme.id}" style="color: inherit;">${escapeHtml(theme.name)}</a>
      </span>
      <span class="rank-desc"> · ${escapeHtml(theme.description)}</span>
    `;
    list.appendChild(li);
  });
}

/* ── Init ── */
(async function init() {
  try {
    const [themes, popular] = await Promise.all([
      api("/themes"),
      api("/themes/popular"),
    ]);
    renderThemes(themes);
    renderPopular(popular);
  } catch (e) {
    setMessage(e.message, true);
  }
})();

$("#refreshPopular").addEventListener("click", async () => {
  try {
    const popular = await api("/themes/popular");
    renderPopular(popular);
    setMessage("인기 테마를 갱신했습니다.");
  } catch (e) {
    setMessage(e.message, true);
  }
});
