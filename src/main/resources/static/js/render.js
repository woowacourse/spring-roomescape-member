import { appEl, modalRootEl } from "./dom.js";
import { indicatorRects, selectedThemeEntity, state, todayString } from "./state.js";

export function render() {
  if (state.role === "user") {
    renderUser();
    return;
  }

  if (state.role === "admin") {
    renderAdmin();
    return;
  }

  renderHome();
}

export function renderHome() {
  appEl.innerHTML = `
    <main class="screen screen-home">
      <section class="hero-glass">
        <div class="hero-copy">
          <p class="eyebrow">ROOMESCAPE</p>
          <h1>조용하고 선명한<br>방탈출 예약</h1>
          <p class="hero-description">원하는 역할을 선택하면 현재 서버의 REST API를 그대로 사용해 예약과 운영 데이터를 다룹니다.</p>
        </div>
        <div class="role-selector" data-indicator-container="role">
          <div class="selection-indicator role-selection-indicator" aria-hidden="true"></div>
          ${roleCard("user", "사용자", "테마 선택부터 예약 완료까지", "인기 테마와 가능한 시간을 보고 빠르게 예약합니다.")}
          ${roleCard("admin", "관리자", "테마와 시간을 정리하기", "운영에 필요한 테마, 예약 시간, 예약 목록을 한 화면에서 관리합니다.")}
        </div>
      </section>
    </main>
  `;
  renderConfirm();
  scheduleIndicators();
}

export function renderUser() {
  appEl.innerHTML = `
    ${topbar("사용자", "예약 가능 시간 조회와 예약 생성")}
    <main class="screen">
      <section class="page-heading">
        <div>
          <p class="eyebrow">USER</p>
          <h1>테마를 고르고 시간을 선택하세요</h1>
          <p>날짜와 테마가 바뀌면 예약 가능 시간이 자동으로 다시 계산됩니다.</p>
        </div>
        <div class="metrics">
          ${metric(state.themes.length, "전체 테마")}
          ${metric(state.popularThemes.length, "인기 테마")}
        </div>
      </section>

      <section class="workspace">
        <div class="content-stack">
          <section class="glass-panel">
            <div class="panel-heading">
              <div>
                <h2>인기 테마</h2>
                <p>어제 기준 최근 1주 동안 예약이 많았던 테마입니다.</p>
              </div>
              <button class="glass-icon-button" type="button" data-action="refresh-user" title="새로고침" aria-label="새로고침">↻</button>
            </div>
            ${renderPopularThemes()}
          </section>

          <section class="glass-panel">
            <div class="panel-heading">
              <div>
                <h2>전체 테마</h2>
                <p>선택한 테마는 예약 패널에 바로 반영됩니다.</p>
              </div>
            </div>
            ${renderThemeGrid(state.themes, {
              selectable: true,
              emptyTitle: "테마가 없습니다",
              emptyDescription: "관리자 화면에서 테마를 먼저 추가해 주세요."
            })}
          </section>
        </div>

        <aside class="glass-panel reservation-sheet" id="reservation-sheet">
          <div class="panel-heading">
            <div>
              <h2>예약 정보</h2>
              <p>테마, 날짜, 시간을 확인하고 예약합니다.</p>
            </div>
          </div>
          <form id="reservation-form" class="form-grid">
            ${renderSelectedTheme()}
            <div class="field">
              <label for="reservation-date">날짜</label>
              <input id="reservation-date" class="input" name="date" type="date" value="${escapeAttr(state.selectedDate)}" min="${todayString()}">
            </div>
            <div class="field">
              <label>시간</label>
              ${renderAvailableTimes()}
            </div>
            <div class="field">
              <label for="reservation-name">예약자 이름</label>
              <input id="reservation-name" class="input" name="name" type="text" value="${escapeAttr(state.reservationName)}" placeholder="이름을 입력하세요" autocomplete="name">
            </div>
            <button class="primary-button full" type="submit" ${state.submitting ? "disabled" : ""}>${state.submitting ? "예약 중" : "예약하기"}</button>
          </form>
        </aside>
      </section>
    </main>
  `;
  renderConfirm();
  scheduleIndicators();
}

export function renderAdmin() {
  appEl.innerHTML = `
    ${topbar("관리자", "테마, 시간, 예약 운영")}
    <main class="screen">
      <section class="page-heading">
        <div>
          <p class="eyebrow">ADMIN</p>
          <h1>운영 데이터를 정돈하세요</h1>
          <p>관리자 API와 예약 API를 사용해 서버 데이터를 즉시 반영합니다.</p>
        </div>
        <div class="metrics">
          ${metric(state.themes.length, "테마")}
          ${metric(state.adminTimes.length, "시간")}
          ${metric(state.reservations.length, "예약")}
        </div>
      </section>

      <section class="workspace admin-workspace">
        <aside class="glass-panel admin-nav">
          <div class="tab-list" data-indicator-container="admin" role="tablist" aria-label="관리 메뉴">
            <div class="selection-indicator admin-selection-indicator" aria-hidden="true"></div>
            ${adminTabButton("themes", "테마 관리")}
            ${adminTabButton("times", "시간 관리")}
            ${adminTabButton("reservations", "예약 관리")}
          </div>
        </aside>
        ${renderAdminTab()}
      </section>
    </main>
  `;
  renderConfirm();
  scheduleIndicators();
}

function roleCard(role, label, title, description) {
  return `
    <button class="role-card ${state.role === role ? "is-active" : ""}" type="button" data-action="choose-role" data-role="${role}">
      <span class="card-kicker">${escapeHtml(label)}</span>
      <strong>${escapeHtml(title)}</strong>
      <span>${escapeHtml(description)}</span>
    </button>
  `;
}

function topbar(roleLabel, description) {
  return `
    <header class="topbar">
      <div class="topbar-inner">
        <div class="brand">
          <span class="brand-mark">R</span>
          <div>
            <p class="brand-title">Roomescape ${escapeHtml(roleLabel)}</p>
            <p class="brand-subtitle">${escapeHtml(description)}</p>
          </div>
        </div>
        <button class="glass-button small" type="button" data-action="go-home">처음으로</button>
      </div>
    </header>
  `;
}

function metric(value, label) {
  return `
    <div class="metric">
      <span>${escapeHtml(value)}</span>
      <small>${escapeHtml(label)}</small>
    </div>
  `;
}

function adminTabButton(tab, label) {
  return `
    <button class="tab-button ${state.adminTab === tab ? "is-active" : ""}" type="button" role="tab" data-action="admin-tab" data-tab="${tab}">
      ${escapeHtml(label)}
    </button>
  `;
}

function renderPopularThemes() {
  if (state.loading.user && state.popularThemes.length === 0) {
    return skeletonCards(5, "popular-row");
  }

  if (state.popularThemes.length === 0) {
    return emptyState("인기 테마가 없습니다", "최근 1주 동안 생성된 예약 데이터가 없으면 이 목록은 비어 있습니다.");
  }

  return `
    <div class="popular-row" data-indicator-container="theme">
      <div class="selection-indicator theme-selection-indicator" aria-hidden="true"></div>
      ${state.popularThemes.map((theme, index) => renderThemeCard(theme, {
        selectable: true,
        rank: index + 1,
        reservedCount: theme.reservedCount
      })).join("")}
    </div>
  `;
}

function renderThemeGrid(themes, options = {}) {
  if (state.loading.user && themes.length === 0) {
    return skeletonCards(4, "theme-grid");
  }

  if (themes.length === 0) {
    return emptyState(options.emptyTitle || "목록이 비어 있습니다", options.emptyDescription || "표시할 데이터가 없습니다.");
  }

  return `
    <div class="theme-grid" data-indicator-container="theme">
      <div class="selection-indicator theme-selection-indicator" aria-hidden="true"></div>
      ${themes.map((theme) => renderThemeCard(theme, options)).join("")}
    </div>
  `;
}

function renderThemeCard(theme, options = {}) {
  const selected = Number(state.selectedThemeId) === Number(theme.id);
  const tagName = options.selectable ? "button" : "article";
  const actionAttrs = options.selectable ? `type="button" data-action="select-theme" data-theme-id="${theme.id}"` : "";
  const countBadge = typeof options.reservedCount === "number"
    ? `<span class="badge green">${options.reservedCount}건</span>`
    : `<span class="badge">ID ${theme.id}</span>`;
  const rankBadge = options.rank ? `<span class="badge blue">${options.rank}위</span>` : "";

  return `
    <${tagName} class="theme-card ${selected ? "is-active" : ""}" ${actionAttrs}>
      ${themeThumb(theme)}
      <div class="theme-body">
        <h3>${escapeHtml(theme.name)}</h3>
        <p>${escapeHtml(theme.description)}</p>
        <div class="badge-row">
          ${rankBadge || countBadge}
          ${rankBadge ? countBadge : ""}
        </div>
      </div>
    </${tagName}>
  `;
}

function themeThumb(theme) {
  const hasImage = Boolean(theme.thumbnailImgUrl);

  return `
    <div class="theme-thumb ${hasImage ? "has-image" : ""}">
      ${hasImage ? `<img src="${escapeAttr(theme.thumbnailImgUrl)}" alt="${escapeAttr(theme.name)} 썸네일" loading="lazy" data-fallback-image>` : ""}
      <span class="theme-thumb-fallback">${escapeHtml(themeFallbackText(theme.name))}</span>
    </div>
  `;
}

function themeFallbackText(name) {
  return String(name || "테마").trim();
}

function renderSelectedTheme() {
  const selectedTheme = selectedThemeEntity();

  if (!selectedTheme) {
    return `<div class="notice">먼저 테마를 선택하면 날짜별 예약 가능 시간이 표시됩니다.</div>`;
  }

  return `
    <div class="selected-theme">
      ${themeThumb(selectedTheme)}
      <div>
        <strong>${escapeHtml(selectedTheme.name)}</strong>
        <span>${escapeHtml(selectedTheme.description)}</span>
      </div>
    </div>
  `;
}

function renderAvailableTimes() {
  if (!state.selectedThemeId) {
    return emptyState("테마를 선택하세요", "예약 시간을 확인하려면 테마를 먼저 선택해야 합니다.");
  }

  if (state.loading.times) {
    return skeletonLines(6, "time-grid");
  }

  if (state.availableTimes.length === 0) {
    return emptyState("시간이 없습니다", "관리자 화면에서 예약 시간을 먼저 추가해 주세요.");
  }

  return `
    <div class="time-grid" data-indicator-container="time">
      <div class="selection-indicator time-selection-indicator" aria-hidden="true"></div>
      ${state.availableTimes.map((time) => `
        <button
          class="time-button ${Number(state.selectedTimeId) === Number(time.id) ? "is-active" : ""} ${time.available ? "" : "is-unavailable"}"
          type="button"
          data-action="select-time"
          data-time-id="${time.id}"
          ${time.available ? "" : "disabled"}
          title="${time.available ? "예약 가능" : "이미 예약됨"}"
        >
          ${escapeHtml(time.startAt)}
        </button>
      `).join("")}
    </div>
  `;
}

function renderAdminTab() {
  if (state.adminTab === "times") {
    return renderAdminTimes();
  }

  if (state.adminTab === "reservations") {
    return renderAdminReservations();
  }

  return renderAdminThemes();
}

function renderAdminThemes() {
  return `
    <section class="admin-panel-grid">
      <section class="glass-panel">
        <div class="panel-heading">
          <div>
            <h2>테마 추가</h2>
            <p>이름, 설명, 썸네일 URL을 입력하세요.</p>
          </div>
        </div>
        <form id="theme-form" class="form-grid">
          <div class="field">
            <label for="theme-name">테마 이름</label>
            <input id="theme-name" class="input" name="name" type="text" value="${escapeAttr(state.themeDraft.name)}" placeholder="예: 추리 탐정사무소" required>
          </div>
          <div class="field">
            <label for="theme-description">설명</label>
            <textarea id="theme-description" class="textarea" name="description" placeholder="테마를 한 문장으로 소개하세요" required>${escapeHtml(state.themeDraft.description)}</textarea>
          </div>
          <div class="field">
            <label for="theme-thumbnail">썸네일 이미지 URL</label>
            <input id="theme-thumbnail" class="input" name="thumbnailImgUrl" type="url" value="${escapeAttr(state.themeDraft.thumbnailImgUrl)}" placeholder="https://..." required>
          </div>
          <button class="primary-button full" type="submit" ${state.submitting ? "disabled" : ""}>테마 추가</button>
        </form>
      </section>
      <section class="glass-panel">
        <div class="panel-heading">
          <div>
            <h2>테마 목록</h2>
            <p>사용자 화면에도 동일하게 노출됩니다.</p>
          </div>
          <button class="glass-icon-button" type="button" data-action="refresh-admin" title="새로고침" aria-label="새로고침">↻</button>
        </div>
        ${renderAdminThemeList()}
      </section>
    </section>
  `;
}

function renderAdminThemeList() {
  if (state.loading.adminThemes && state.themes.length === 0) {
    return skeletonLines(6, "list");
  }

  if (state.themes.length === 0) {
    return emptyState("테마가 없습니다", "새 테마를 추가하면 여기에 표시됩니다.");
  }

  return `
    <div class="list">
      ${state.themes.map((theme) => `
        <article class="list-item">
          <div>
            <h3>${escapeHtml(theme.name)}</h3>
            <p>${escapeHtml(theme.description)}</p>
          </div>
          <button class="danger-button" type="button" data-action="delete-theme" data-theme-id="${theme.id}">삭제</button>
        </article>
      `).join("")}
    </div>
  `;
}

function renderAdminTimes() {
  return `
    <section class="admin-panel-grid">
      <section class="glass-panel">
        <div class="panel-heading">
          <div>
            <h2>예약 시간 추가</h2>
            <p>서버는 HH:mm 형식을 받습니다.</p>
          </div>
        </div>
        <form id="time-form" class="form-grid">
          <div class="field">
            <label for="time-start-at">시작 시간</label>
            <input id="time-start-at" class="input" name="startAt" type="time" value="${escapeAttr(state.timeDraft.startAt)}" step="60" required>
          </div>
          <button class="primary-button full" type="submit" ${state.submitting ? "disabled" : ""}>시간 추가</button>
        </form>
      </section>
      <section class="glass-panel">
        <div class="panel-heading">
          <div>
            <h2>예약 시간 목록</h2>
            <p>삭제된 시간은 이후 예약 선택지에서 사라집니다.</p>
          </div>
          <button class="glass-icon-button" type="button" data-action="refresh-admin" title="새로고침" aria-label="새로고침">↻</button>
        </div>
        ${renderAdminTimeList()}
      </section>
    </section>
  `;
}

function renderAdminTimeList() {
  if (state.loading.adminTimes && state.adminTimes.length === 0) {
    return skeletonLines(6, "list");
  }

  if (state.adminTimes.length === 0) {
    return emptyState("예약 시간이 없습니다", "새 예약 시간을 추가하면 사용자 화면에서 선택할 수 있습니다.");
  }

  return `
    <div class="list">
      ${state.adminTimes.map((time) => `
        <article class="list-item">
          <div>
            <h3>${escapeHtml(time.startAt)}</h3>
            <p>예약 시간 ID ${time.id}</p>
          </div>
          <button class="danger-button" type="button" data-action="delete-time" data-time-id="${time.id}">삭제</button>
        </article>
      `).join("")}
    </div>
  `;
}

function renderAdminReservations() {
  return `
    <section class="glass-panel">
      <div class="panel-heading">
        <div>
          <h2>전체 예약</h2>
          <p>날짜 오름차순으로 정렬된 서버 예약 목록입니다.</p>
        </div>
        <button class="glass-icon-button" type="button" data-action="refresh-admin" title="새로고침" aria-label="새로고침">↻</button>
      </div>
      ${renderReservationList()}
    </section>
  `;
}

function renderReservationList() {
  if (state.loading.reservations && state.reservations.length === 0) {
    return skeletonLines(6, "reservation-list");
  }

  if (state.reservations.length === 0) {
    return emptyState("예약이 없습니다", "사용자 화면에서 예약을 만들면 여기에 표시됩니다.");
  }

  return `
    <div class="reservation-list">
      ${state.reservations.map((reservation) => `
        <article class="reservation-item">
          <div class="reservation-main">
            <div class="reservation-date">${formatShortDate(reservation.date)}</div>
            <div>
              <h3>${escapeHtml(reservation.name)}</h3>
              <p>${escapeHtml(reservation.theme?.name || "테마 없음")} · ${escapeHtml(reservation.time?.startAt || "시간 없음")} · ID ${reservation.id}</p>
            </div>
          </div>
          <button class="danger-button" type="button" data-action="delete-reservation" data-reservation-id="${reservation.id}">삭제</button>
        </article>
      `).join("")}
    </div>
  `;
}

function renderConfirm() {
  if (!state.confirm) {
    modalRootEl.innerHTML = "";
    return;
  }

  modalRootEl.innerHTML = `
    <div class="modal-backdrop" data-action="confirm-cancel">
      <section class="confirm-sheet" role="dialog" aria-modal="true" aria-labelledby="confirm-title">
        <p class="eyebrow">CONFIRM</p>
        <h2 id="confirm-title">${escapeHtml(state.confirm.title)}</h2>
        <p>${escapeHtml(state.confirm.message)}</p>
        <div class="confirm-actions">
          <button class="glass-button" type="button" data-action="confirm-cancel">취소</button>
          <button class="danger-button strong" type="button" data-action="confirm-ok">삭제</button>
        </div>
      </section>
    </div>
  `;
}

function emptyState(title, description) {
  return `
    <div class="empty-state">
      <strong>${escapeHtml(title)}</strong>
      <span>${escapeHtml(description)}</span>
    </div>
  `;
}

function skeletonCards(count, className) {
  return `
    <div class="${className}">
      ${Array.from({ length: count }, () => `<div class="skeleton skeleton-card"></div>`).join("")}
    </div>
  `;
}

function skeletonLines(count, className) {
  return `
    <div class="${className}">
      ${Array.from({ length: count }, () => `<div class="skeleton skeleton-line"></div>`).join("")}
    </div>
  `;
}

function scheduleIndicators() {
  requestAnimationFrame(() => {
    moveSelectionIndicator("role", ".role-card.is-active", "role-selection-indicator");
    moveSelectionIndicator("admin", ".tab-button.is-active", "admin-selection-indicator");
    moveSelectionIndicator("theme", ".theme-card.is-active", "theme-selection-indicator");
    moveSelectionIndicator("time", ".time-button.is-active", "time-selection-indicator");
  });
}

function moveSelectionIndicator(containerName, activeSelector, indicatorClass) {
  document.querySelectorAll(`[data-indicator-container="${containerName}"]`).forEach((container, index) => {
    const indicator = container.querySelector(`.${indicatorClass}`);
    const active = container.querySelector(activeSelector);
    if (!indicator || !active) {
      return;
    }

    const containerRect = container.getBoundingClientRect();
    const activeRect = active.getBoundingClientRect();
    const nextRect = {
      x: activeRect.left - containerRect.left + container.scrollLeft,
      y: activeRect.top - containerRect.top + container.scrollTop,
      width: active.offsetWidth,
      height: active.offsetHeight
    };
    const rectKey = `${containerName}-${index}`;
    const previousRect = indicatorRects[rectKey] || nextRect;

    indicator.classList.remove("is-ready");
    setIndicatorVars(indicator, previousRect);

    requestAnimationFrame(() => {
      indicator.classList.add("is-ready");
      setIndicatorVars(indicator, nextRect);
      indicatorRects[rectKey] = nextRect;
    });
  });
}

function setIndicatorVars(indicator, rect) {
  indicator.style.setProperty("--indicator-x", `${rect.x}px`);
  indicator.style.setProperty("--indicator-y", `${rect.y}px`);
  indicator.style.setProperty("--indicator-width", `${rect.width}px`);
  indicator.style.setProperty("--indicator-height", `${rect.height}px`);
}

function formatShortDate(value) {
  const [, month, day] = String(value).split("-");
  return `${Number(month)}/${Number(day)}`;
}

export function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function escapeAttr(value) {
  return escapeHtml(value);
}
