// ===== my-reservations.js — 내 예약 페이지 전용 =====

const state = {
  currentName: '',
  availableDates: [],
  modifyingReservationId: null,
  modifyingThemeId: null,
  modifyingThemeName: null,
  
  selectedDate: null,
  selectedTimeId: null,
  selectedTimeLabel: null,
  
  currentCalendarYear: new Date().getFullYear(),
  currentCalendarMonth: new Date().getMonth(),
};

const $ = id => document.getElementById(id);

// ===== Toast =====
function showToast(msg, type = 'default') {
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.textContent = msg;
  $('toast-container').appendChild(el);
  setTimeout(() => el.remove(), 3100);
}

// ===== API =====
const api = {
  async request(url, options) {
    const res = await fetch(url, options);

    if (!res.ok) {
      let errorMsg = `요청 실패 (${res.status})`;
      try {
        const errorData = await res.json();
        if (errorData.message) {
          errorMsg = errorData.message;
        }
      } catch (e) {
        const text = await res.text();
        if (text) errorMsg = text;
      }
      throw new Error(errorMsg);
    }

    if (res.status === 204) return null;
    return res.json();
  },

  get(url) {
    return this.request(url);
  },
  
  patch(url, body) {
    return this.request(url, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
  },

  del(url) {
    return this.request(url, { method: 'DELETE' });
  }
};

function formatTime(t) {
  if (!t) return '';
  if (Array.isArray(t)) return `${String(t[0]).padStart(2,'0')}:${String(t[1]).padStart(2,'0')}`;
  return t.substring(0, 5);
}

// ===== Load My Reservations =====
async function loadMyReservations() {
  const name = $('search-name').value.trim();
  if (!name) {
    showToast('이름을 입력해주세요.', 'error');
    return;
  }
  state.currentName = name;
  
  const tbody = $('my-reservations-tbody');
  tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:32px;color:var(--text-muted)">불러오는 중...</td></tr>`;
  
  try {
    const data = await api.get(`/reservations?name=${encodeURIComponent(name)}`);
    if (!data || data.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:32px;color:var(--text-muted)">예약 내역이 없습니다.</td></tr>`;
      return;
    }
    
    tbody.innerHTML = data.map(r => `
      <tr>
        <td>${r.id}</td>
        <td>${r.date}</td>
        <td>${formatTime(r.time.startAt)}</td>
        <td>${r.theme.name}</td>
        <td><button class="btn-ghost" style="padding:4px 12px;font-size:11px" onclick="openModifyModal(${r.id}, '${r.theme.name}', ${r.theme.id})">변경</button></td>
        <td><button class="btn-delete" onclick="deleteReservation(${r.id})">취소</button></td>
      </tr>
    `).join('');
  } catch(e) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:32px;color:var(--text-muted)">불러오기에 실패했습니다.</td></tr>`;
    showToast(e.message, 'error');
  }
}

async function deleteReservation(id) {
  if (!confirm('정말 이 예약을 취소하시겠습니까?')) return;
  try {
    await api.del(`/reservations/${id}?name=${encodeURIComponent(state.currentName)}`);
    showToast('예약이 취소되었습니다.', 'success');
    loadMyReservations();
  } catch (e) {
    showToast(e.message, 'error');
  }
}

// ===== Modify Modal =====

async function openModifyModal(reservationId, themeName, themeId) {
  state.modifyingReservationId = reservationId;
  state.modifyingThemeName = themeName;
  state.modifyingThemeId = themeId;
  state.selectedDate = null;
  state.selectedTimeId = null;
  state.selectedTimeLabel = null;
  
  $('modify-modal-theme').textContent = themeName;
  updateModifyCTAInfo();
  
  try {
    const datesData = await api.get('/reservations/available-dates');
    state.availableDates = datesData.dates || datesData || [];
    renderModifyCalendar();
    loadModifyTimeSlots(); // will show empty state
    $('modify-modal').classList.add('open');
  } catch(e) {
    showToast('예약 정보를 불러오지 못했습니다. ' + e.message, 'error');
  }
}

function closeModifyModal() { 
  $('modify-modal').classList.remove('open'); 
}

function updateModifyCTAInfo() {
  const btn = $('confirm-modify-btn');
  $('modify-modal-date').textContent = state.selectedDate || '—';
  $('modify-modal-time').textContent = state.selectedTimeLabel || '—';
  btn.disabled = !(state.selectedDate && state.selectedTimeId);
}

function toISODate(date) {
  return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
}

function renderModifyCalendar() {
  const { currentCalendarYear: y, currentCalendarMonth: m, availableDates } = state;
  const monthNames = ['January','February','March','April','May','June',
                      'July','August','September','October','November','December'];
  $('modify-cal-month-label').textContent = `${monthNames[m]} ${y}`;

  const grid = $('modify-calendar-grid');
  grid.querySelectorAll('.calendar-day').forEach(el => el.remove());

  const firstDay = new Date(y, m, 1).getDay();
  const daysInMonth = new Date(y, m + 1, 0).getDate();
  const today = new Date(); today.setHours(0, 0, 0, 0);
  const availableSet = new Set(availableDates);

  for (let i = 0; i < firstDay; i++) {
    const el = document.createElement('div');
    el.className = 'calendar-day empty';
    grid.appendChild(el);
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const date = new Date(y, m, d);
    const dateStr = toISODate(date);
    const el = document.createElement('div');
    el.className = 'calendar-day';
    el.textContent = d;
    el.dataset.date = dateStr;

    if (date < today || !availableSet.has(dateStr)) {
      el.classList.add('disabled');
    } else {
      el.classList.add('available');
      el.addEventListener('click', () => selectModifyDate(dateStr, el));
    }
    if (date.getTime() === today.getTime()) el.classList.add('today');
    if (state.selectedDate === dateStr) el.classList.add('selected');
    grid.appendChild(el);
  }
}

function selectModifyDate(dateStr, el) {
  state.selectedDate = dateStr;
  document.querySelectorAll('#modify-calendar-grid .calendar-day').forEach(d => d.classList.remove('selected'));
  el.classList.add('selected');
  updateModifyCTAInfo();
  loadModifyTimeSlots();
}

async function loadModifyTimeSlots() {
  const { selectedDate, modifyingThemeId } = state;
  const container = $('modify-time-slots-container');

  if (!selectedDate || !modifyingThemeId) {
    container.innerHTML = `<div class="empty-state"><p>🗓</p><p>날짜를<br>먼저 선택해주세요.</p></div>`;
    return;
  }

  container.innerHTML = `<div class="time-grid">${[1,2,3,4].map(() =>
    `<div class="skeleton" style="height:60px"></div>`).join('')}</div>`;

  try {
    const times = await api.get(`/reservations/available-times?date=${selectedDate}&themeId=${modifyingThemeId}`);
    state.selectedTimeId = null;
    state.selectedTimeLabel = null;

    if (!times || times.length === 0) {
      container.innerHTML = `<div class="empty-state"><p>⏰</p><p>예약 가능한<br>시간대가 없습니다.</p></div>`;
      return;
    }

    const grid = document.createElement('div');
    grid.className = 'time-grid';
    times.forEach(t => {
      const slot = document.createElement('div');
      slot.className = 'time-slot' + (t.reserved ? ' reserved' : '');
      const label = formatTime(t.startAt);
      slot.innerHTML = `<div>${label}</div><div class="time-slot-badge">${t.reserved ? 'RESERVED' : 'AVAILABLE'}</div>`;
      if (!t.reserved) slot.addEventListener('click', () => selectModifyTime(t.id, label, slot));
      grid.appendChild(slot);
    });

    container.innerHTML = '';
    container.appendChild(grid);
  } catch(e) {
    container.innerHTML = `<div class="empty-state"><p>⚠️</p><p>시간대를<br>불러오지 못했습니다.</p></div>`;
  }
  updateModifyCTAInfo();
}

function selectModifyTime(id, label, el) {
  state.selectedTimeId = id;
  state.selectedTimeLabel = label;
  document.querySelectorAll('#modify-time-slots-container .time-slot:not(.reserved)').forEach(s => s.classList.remove('selected'));
  el.classList.add('selected');
  updateModifyCTAInfo();
}

async function submitModifyBooking() {
  if (!state.selectedDate || !state.selectedTimeId) {
    showToast('변경할 날짜와 시간을 선택해주세요.', 'error');
    return;
  }

  const btn = $('confirm-modify-btn');
  btn.disabled = true; btn.textContent = '변경 중...';
  try {
    // Controller 에서는 @RequestParam 으로 받으므로 URL 파라미터로 전송
    await api.patch(`/reservations/${state.modifyingReservationId}?name=${encodeURIComponent(state.currentName)}&date=${state.selectedDate}&timeId=${state.selectedTimeId}`);
    closeModifyModal();
    showToast('예약이 성공적으로 변경되었습니다! 🎉', 'success');
    loadMyReservations();
  } catch (e) {
    showToast('예약 변경에 실패했습니다. ' + e.message, 'error');
  } finally {
    btn.disabled = false; btn.textContent = '변경하기';
  }
}

// ===== Init =====
document.addEventListener('DOMContentLoaded', () => {
  $('btn-search-reservation').addEventListener('click', loadMyReservations);
  
  // 엔터로 조회
  $('search-name').addEventListener('keyup', (e) => {
    if (e.key === 'Enter') loadMyReservations();
  });

  // Calendar nav
  $('modify-cal-prev').addEventListener('click', () => {
    state.currentCalendarMonth--;
    if (state.currentCalendarMonth < 0) { state.currentCalendarMonth = 11; state.currentCalendarYear--; }
    renderModifyCalendar();
  });
  $('modify-cal-next').addEventListener('click', () => {
    state.currentCalendarMonth++;
    if (state.currentCalendarMonth > 11) { state.currentCalendarMonth = 0; state.currentCalendarYear++; }
    renderModifyCalendar();
  });

  // Modal
  $('modify-modal-close-btn').addEventListener('click', closeModifyModal);
  $('modify-modal-cancel-btn').addEventListener('click', closeModifyModal);
  $('modify-modal').addEventListener('click', e => { if (e.target === $('modify-modal')) closeModifyModal(); });
  $('confirm-modify-btn').addEventListener('click', submitModifyBooking);
});
