// ===== State =====
const state = {
  selectedDate: null,
  selectedThemeId: null,
  selectedThemeName: null,
  selectedTimeId: null,
  selectedTimeLabel: null,
  themes: [],
  times: [],
  currentCalendarYear: new Date().getFullYear(),
  currentCalendarMonth: new Date().getMonth(),
  availableDates: [],
};

// ===== DOM Refs =====
const $ = id => document.getElementById(id);
const qs = (sel, ctx = document) => ctx.querySelector(sel);

// ===== Toast =====
function showToast(msg, type = 'default') {
  const container = $('toast-container');
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.textContent = msg;
  container.appendChild(el);
  setTimeout(() => el.remove(), 3100);
}

// ===== API =====
const api = {
  async get(url) {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`GET ${url} failed: ${res.status}`);
    return res.json();
  },
  async post(url, body) {
    const res = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || `POST ${url} failed: ${res.status}`);
    }
    return res.json();
  },
  async del(url) {
    const res = await fetch(url, { method: 'DELETE' });
    if (!res.ok) throw new Error(`DELETE ${url} failed: ${res.status}`);
    return res;
  },
};

// ===== Navigation =====
function switchPage(pageId) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
  $(pageId).classList.add('active');
  document.querySelector(`[data-page="${pageId}"]`).classList.add('active');
  if (pageId === 'admin-page') initAdminPage();
}

// ===== CALENDAR =====
function renderCalendar() {
  const { currentCalendarYear: y, currentCalendarMonth: m, availableDates } = state;
  const monthNames = ['January','February','March','April','May','June',
                      'July','August','September','October','November','December'];

  $('cal-month-label').textContent = `${monthNames[m]} ${y}`;

  const grid = $('calendar-grid');
  // clear day cells (keep day-label headers)
  grid.querySelectorAll('.calendar-day').forEach(el => el.remove());

  const firstDay = new Date(y, m, 1).getDay(); // 0=Sun
  const daysInMonth = new Date(y, m + 1, 0).getDate();

  // empty leading cells
  for (let i = 0; i < firstDay; i++) {
    const el = document.createElement('div');
    el.className = 'calendar-day empty';
    grid.appendChild(el);
  }

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const availableSet = new Set(availableDates);

  for (let d = 1; d <= daysInMonth; d++) {
    const date = new Date(y, m, d);
    const dateStr = toISODate(date);
    const isToday = date.getTime() === today.getTime();
    const isPast = date < today;
    const isAvailable = availableSet.has(dateStr);

    const el = document.createElement('div');
    el.className = 'calendar-day';
    el.textContent = d;
    el.dataset.date = dateStr;

    if (isPast || !isAvailable) {
      el.classList.add('disabled');
    } else {
      el.classList.add('available');
      el.addEventListener('click', () => selectDate(dateStr, el));
    }

    if (isToday) el.classList.add('today');
    if (state.selectedDate === dateStr) el.classList.add('selected');

    grid.appendChild(el);
  }
}

function selectDate(dateStr, el) {
  if (el.classList.contains('disabled')) return;
  state.selectedDate = dateStr;
  document.querySelectorAll('.calendar-day').forEach(d => d.classList.remove('selected'));
  el.classList.add('selected');
  updateCTAInfo();
  loadTimeSlots();
}

function toISODate(date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, '0');
  const d = String(date.getDate()).padStart(2, '0');
  return `${y}-${m}-${d}`;
}

// ===== THEMES =====
async function loadThemes() {
  const opts = await api.get('/reservations/date-and-theme');
  state.availableDates = opts.dates || [];
  state.themes = opts.themes || [];
  renderCalendar();
  renderThemes();
}

function renderThemes() {
  const list = $('theme-list');
  list.innerHTML = '';
  state.themes.forEach(theme => {
    const card = document.createElement('div');
    card.className = 'theme-card' + (state.selectedThemeId === theme.id ? ' selected' : '');
    card.dataset.id = theme.id;
    card.innerHTML = `
      <img class="theme-thumb" src="${theme.thumbnailUrl}" alt="${theme.name}" onerror="this.style.display='none'">
      <div class="theme-info">
        <div class="theme-name">${theme.name}</div>
        <div class="theme-desc">${theme.description}</div>
      </div>
      <div class="theme-check">${state.selectedThemeId === theme.id ? '✓' : ''}</div>
    `;
    card.addEventListener('click', () => selectTheme(theme, card));
    list.appendChild(card);
  });
}

function selectTheme(theme, card) {
  state.selectedThemeId = theme.id;
  state.selectedThemeName = theme.name;
  document.querySelectorAll('.theme-card').forEach(c => {
    c.classList.remove('selected');
    qs('.theme-check', c).textContent = '';
  });
  card.classList.add('selected');
  qs('.theme-check', card).textContent = '✓';
  updateCTAInfo();
  loadTimeSlots();
}

// ===== TIME SLOTS =====
async function loadTimeSlots() {
  const { selectedDate, selectedThemeId } = state;
  const container = $('time-slots-container');

  if (!selectedDate || !selectedThemeId) {
    container.innerHTML = `<div class="empty-state"><p>🗓</p><p>날짜와 테마를 먼저 선택해주세요.</p></div>`;
    return;
  }

  container.innerHTML = `<div class="time-grid">
    ${[1,2,3,4].map(() => `<div class="skeleton" style="height:64px"></div>`).join('')}
  </div>`;

  const times = await api.get(`/reservations/available-times?date=${selectedDate}&themeId=${selectedThemeId}`);
  state.times = times;
  state.selectedTimeId = null;
  state.selectedTimeLabel = null;

  if (!times || times.length === 0) {
    container.innerHTML = `<div class="empty-state"><p>⏰</p><p>예약 가능한 시간대가 없습니다.</p></div>`;
    return;
  }

  const grid = document.createElement('div');
  grid.className = 'time-grid';

  times.forEach(t => {
    const slot = document.createElement('div');
    slot.className = 'time-slot' + (t.reserved ? ' reserved' : '');
    slot.dataset.id = t.id;
    const timeLabel = formatTime(t.startAt);
    slot.innerHTML = `
      <div>${timeLabel}</div>
      <div class="time-slot-badge">${t.reserved ? 'RESERVED' : 'AVAILABLE'}</div>
    `;
    if (!t.reserved) {
      slot.addEventListener('click', () => selectTime(t.id, timeLabel, slot));
    }
    grid.appendChild(slot);
  });

  container.innerHTML = '';
  container.appendChild(grid);
  updateCTAInfo();
}

function selectTime(id, label, el) {
  state.selectedTimeId = id;
  state.selectedTimeLabel = label;
  document.querySelectorAll('.time-slot:not(.reserved)').forEach(s => s.classList.remove('selected'));
  el.classList.add('selected');
  updateCTAInfo();
}

function formatTime(t) {
  if (!t) return '';
  // t is like "10:00:00" or [10, 0] depending on serialization
  if (Array.isArray(t)) return `${String(t[0]).padStart(2,'0')}:${String(t[1]).padStart(2,'0')}`;
  return t.substring(0, 5);
}

// ===== CTA =====
function updateCTAInfo() {
  const info = $('cta-info');
  const btn = $('book-btn');
  const parts = [];
  if (state.selectedDate) parts.push(`<strong>${state.selectedDate}</strong>`);
  if (state.selectedThemeName) parts.push(`<strong>${state.selectedThemeName}</strong>`);
  if (state.selectedTimeLabel) parts.push(`<strong>${state.selectedTimeLabel}</strong>`);

  if (parts.length > 0) {
    info.innerHTML = parts.join(' &mdash; ');
  } else {
    info.innerHTML = '날짜, 테마, 시간을 선택하세요.';
  }

  btn.disabled = !(state.selectedDate && state.selectedThemeId && state.selectedTimeId);
}

// ===== MODAL =====
function openBookingModal() {
  $('modal-date').textContent = state.selectedDate;
  $('modal-theme').textContent = state.selectedThemeName;
  $('modal-time').textContent = state.selectedTimeLabel;
  $('booking-name').value = '';
  $('booking-modal').classList.add('open');
}

function closeBookingModal() {
  $('booking-modal').classList.remove('open');
}

async function submitBooking() {
  const name = $('booking-name').value.trim();
  if (!name) {
    showToast('이름을 입력해주세요.', 'error');
    return;
  }

  const btn = $('confirm-booking-btn');
  btn.disabled = true;
  btn.textContent = '예약 중...';

  try {
    await api.post('/reservations', {
      name,
      date: state.selectedDate,
      timeId: state.selectedTimeId,
      themeId: state.selectedThemeId,
    });
    closeBookingModal();
    showToast('예약이 완료되었습니다! 🎉', 'success');
    // reset selection
    state.selectedTimeId = null;
    state.selectedTimeLabel = null;
    loadTimeSlots();
    updateCTAInfo();
  } catch (e) {
    showToast('예약에 실패했습니다. ' + e.message, 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = '예약하기';
  }
}

// ===== ADMIN =====
let adminCurrentPanel = 'reservations';

function initAdminPage() {
  loadAdminReservations();
}

function switchAdminPanel(panel) {
  adminCurrentPanel = panel;
  document.querySelectorAll('.admin-panel').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.admin-tab').forEach(t => t.classList.remove('active'));
  $(`admin-panel-${panel}`).classList.add('active');
  document.querySelector(`[data-panel="${panel}"]`).classList.add('active');

  if (panel === 'reservations') loadAdminReservations();
  if (panel === 'times') loadAdminTimes();
  if (panel === 'themes') loadAdminThemes();
}

// Admin: Reservations
async function loadAdminReservations() {
  const tbody = $('admin-reservations-tbody');
  tbody.innerHTML = `<tr><td colspan="6" class="empty-state" style="text-align:center;padding:24px;color:var(--text-muted)">불러오는 중...</td></tr>`;
  const data = await api.get('/admin/reservations');
  if (!data.length) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:24px;color:var(--text-muted)">예약 내역이 없습니다.</td></tr>`;
    return;
  }
  tbody.innerHTML = data.map(r => `
    <tr>
      <td>${r.id}</td>
      <td>${r.name}</td>
      <td>${r.date}</td>
      <td>${formatTime(r.time.startAt)}</td>
      <td>${r.theme.name}</td>
      <td><button class="btn-delete" onclick="deleteReservation(${r.id})">삭제</button></td>
    </tr>
  `).join('');
}

async function deleteReservation(id) {
  if (!confirm('이 예약을 삭제하시겠습니까?')) return;
  await api.del(`/reservations/${id}`);
  showToast('예약이 삭제되었습니다.', 'success');
  loadAdminReservations();
}

// Admin: Times
async function loadAdminTimes() {
  const tbody = $('admin-times-tbody');
  tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;padding:24px;color:var(--text-muted)">불러오는 중...</td></tr>`;
  const data = await api.get('/times');
  if (!data.length) {
    tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;padding:24px;color:var(--text-muted)">등록된 시간대가 없습니다.</td></tr>`;
    return;
  }
  tbody.innerHTML = data.map(t => `
    <tr>
      <td>${t.id}</td>
      <td>${formatTime(t.startAt)}</td>
      <td><button class="btn-delete" onclick="deleteTime(${t.id})">삭제</button></td>
    </tr>
  `).join('');
}

async function addTime() {
  const startAt = $('new-time-start').value;
  if (!startAt) { showToast('시작 시간을 입력해주세요.', 'error'); return; }
  await api.post('/times', { startAt });
  showToast('시간대가 추가되었습니다.', 'success');
  $('new-time-start').value = '';
  loadAdminTimes();
}

async function deleteTime(id) {
  if (!confirm('이 시간대를 삭제하시겠습니까?')) return;
  await api.del(`/times/${id}`);
  showToast('시간대가 삭제되었습니다.', 'success');
  loadAdminTimes();
}

// Admin: Themes
async function loadAdminThemes() {
  const tbody = $('admin-themes-tbody');
  tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--text-muted)">불러오는 중...</td></tr>`;
  const data = await api.get('/themes/popular');
  if (!data.length) {
    tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--text-muted)">등록된 테마가 없습니다.</td></tr>`;
    return;
  }
  tbody.innerHTML = data.map(t => `
    <tr>
      <td>${t.id}</td>
      <td>
        <img class="theme-mini-thumb" src="${t.thumbnailUrl}" alt="${t.name}" onerror="this.style.display='none'">
        ${t.name}
      </td>
      <td style="max-width:300px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${t.description}</td>
      <td><button class="btn-delete" onclick="deleteTheme(${t.id})">삭제</button></td>
    </tr>
  `).join('');
}

async function addTheme() {
  const name = $('new-theme-name').value.trim();
  const description = $('new-theme-desc').value.trim();
  const thumbnailUrl = $('new-theme-thumb').value.trim();
  if (!name || !description || !thumbnailUrl) {
    showToast('모든 항목을 입력해주세요.', 'error'); return;
  }
  await api.post('/themes', { name, description, thumbnailUrl });
  showToast('테마가 추가되었습니다.', 'success');
  $('new-theme-name').value = '';
  $('new-theme-desc').value = '';
  $('new-theme-thumb').value = '';
  loadAdminThemes();
}

async function deleteTheme(id) {
  if (!confirm('이 테마를 삭제하시겠습니까?')) return;
  await api.del(`/themes/${id}`);
  showToast('테마가 삭제되었습니다.', 'success');
  loadAdminThemes();
}

// ===== INIT =====
document.addEventListener('DOMContentLoaded', () => {
  // Nav tab switching
  document.querySelectorAll('.nav-tab').forEach(btn => {
    btn.addEventListener('click', () => switchPage(btn.dataset.page));
  });

  // Calendar nav
  $('cal-prev').addEventListener('click', () => {
    state.currentCalendarMonth--;
    if (state.currentCalendarMonth < 0) {
      state.currentCalendarMonth = 11;
      state.currentCalendarYear--;
    }
    renderCalendar();
  });
  $('cal-next').addEventListener('click', () => {
    state.currentCalendarMonth++;
    if (state.currentCalendarMonth > 11) {
      state.currentCalendarMonth = 0;
      state.currentCalendarYear++;
    }
    renderCalendar();
  });

  // Book button
  $('book-btn').addEventListener('click', openBookingModal);

  // Modal close
  $('modal-close-btn').addEventListener('click', closeBookingModal);
  $('modal-cancel-btn').addEventListener('click', closeBookingModal);
  $('booking-modal').addEventListener('click', e => {
    if (e.target === $('booking-modal')) closeBookingModal();
  });

  // Confirm booking
  $('confirm-booking-btn').addEventListener('click', submitBooking);

  // Admin tabs
  document.querySelectorAll('.admin-tab').forEach(btn => {
    btn.addEventListener('click', () => switchAdminPanel(btn.dataset.panel));
  });

  // Admin forms
  $('btn-add-time').addEventListener('click', addTime);
  $('btn-add-theme').addEventListener('click', addTheme);

  // Load user page data
  loadThemes();
  updateCTAInfo();
});
