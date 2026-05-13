import { api, getNextThemeThumbnail, getThemeThumbnail } from './api.js';
import { ui } from './ui.js';

const themeList = document.getElementById('theme-list');
const timeList = document.getElementById('time-list');
const reservationList = document.getElementById('reservation-list');

let allReservations = [];

const escapeHtml = (value) => String(value ?? '')
  .replaceAll('&', '&amp;')
  .replaceAll('<', '&lt;')
  .replaceAll('>', '&gt;')
  .replaceAll('"', '&quot;')
  .replaceAll("'", '&#039;');

const escapeCssUrl = (value) => String(value ?? '').replaceAll("'", '%27');

const loadThemes = async () => {
  try {
    const themes = await api.getThemes();

    if (themes.length === 0) {
      themeList.innerHTML = '<div class="item muted">등록된 테마가 없습니다.</div>';
      return;
    }

    themeList.innerHTML = themes.map((theme, index) => {
      const thumbnail = getThemeThumbnail(theme, index);
      return `
        <div class="item">
          <div class="item-thumbnail" style="background-image: url('${escapeCssUrl(thumbnail)}')"></div>
          <div class="item-info">
            <span class="item-name">${escapeHtml(theme.name)}</span>
            <span class="item-meta">${escapeHtml(theme.description)}</span>
            <span class="item-meta">${escapeHtml(thumbnail)}</span>
          </div>
          <button class="btn-delete" data-id="${theme.id}" data-type="theme">삭제</button>
        </div>
      `;
    }).join('');
  } catch (error) {
    themeList.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
  }
};

const loadTimes = async () => {
  try {
    const times = await api.getTimes();

    if (times.length === 0) {
      timeList.innerHTML = '<div class="item muted">등록된 시간이 없습니다.</div>';
      return;
    }

    timeList.innerHTML = times.map(time => {
      const displayTime = (time.startAt || '').split(':').slice(0, 2).join(':');
      return `
        <div class="item">
          <div class="item-info">
            <span class="item-name">${escapeHtml(displayTime)}</span>
          </div>
          <button class="btn-delete" data-id="${time.id}" data-type="time">삭제</button>
        </div>
      `;
    }).join('');
  } catch (error) {
    timeList.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
  }
};

const loadReservations = async () => {
  try {
    allReservations = await api.getReservations();

    if (allReservations.length === 0) {
      reservationList.innerHTML = '<div class="item muted">예약 내역이 없습니다.</div>';
      return;
    }

    reservationList.innerHTML = allReservations.map(reservation => {
      const displayTime = (reservation.time?.startAt || '').split(':').slice(0, 2).join(':');
      return `
        <div class="item" id="res-item-${reservation.id}">
          <div class="item-info">
            <span class="item-name">${escapeHtml(reservation.name)} · ${escapeHtml(reservation.theme?.name)}</span>
            <span class="item-meta">${escapeHtml(reservation.date)} ${escapeHtml(displayTime)}</span>
          </div>
          <div class="res-actions" style="display: flex; gap: 8px; margin-top: 8px;">
            <button class="btn-delete" data-id="${reservation.id}" data-type="reservation" style="flex: 1; margin-top: 0;">삭제</button>
            <button class="btn-edit-res btn-primary" data-id="${reservation.id}" style="flex: 1; margin-top: 0; padding: 10px; font-size: 14px;">수정</button>
          </div>
          <div id="admin-edit-panel-${reservation.id}" class="edit-panel" style="display: none; margin-top: 15px; padding-top: 15px; border-top: 1px solid var(--color-border);">
            <div class="form-group">
              <label>날짜 수정</label>
              <div class="custom-date-display" onclick="pickAdminDate(${reservation.id})">
                <span id="admin-edit-date-text-${reservation.id}">${reservation.date}</span>
                <span class="icon">▼</span>
              </div>
              <input type="hidden" id="admin-edit-date-${reservation.id}" value="${reservation.date}">
            </div>
            <div class="form-group">
              <label>시간 수정</label>
              <div id="admin-edit-slots-${reservation.id}" class="time-slots"></div>
            </div>
            <button class="btn-primary full-width" onclick="saveAdminReservationUpdate(${reservation.id})" id="admin-save-btn-${reservation.id}" disabled>수정 완료</button>
          </div>
        </div>
      `;
    }).join('');
  } catch (error) {
    reservationList.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
  }
};

window.pickAdminDate = async (id) => {
  const input = document.getElementById(`admin-edit-date-${id}`);
  const text = document.getElementById(`admin-edit-date-text-${id}`);
  const picked = await ui.pickDate(input.value);
  if (picked) {
    input.value = picked;
    text.textContent = picked;
    loadAdminEditSlots(id);
  }
};

const loadAdminEditSlots = async (id) => {
  const date = document.getElementById(`admin-edit-date-${id}`).value;
  const container = document.getElementById(`admin-edit-slots-${id}`);
  const saveBtn = document.getElementById(`admin-save-btn-${id}`);
  const reservation = allReservations.find(r => r.id === id);

  container.innerHTML = '<div class="item muted">로딩 중...</div>';
  saveBtn.disabled = true;

  try {
    const slots = await api.getReservableTimes(date, reservation.theme.id);
    container.innerHTML = slots.map(slot => {
      const isOriginal = String(reservation.time.id) === String(slot.timeId) && reservation.date === date;
      const available = slot.available || isOriginal;
      return `
        <div class="time-slot ${available ? '' : 'disabled'} ${isOriginal ? 'selected' : ''}" 
             onclick="${available ? `selectAdminEditTime(${id}, ${slot.timeId})` : ''}"
             data-id="${slot.timeId}">
          ${slot.startAt.split(':').slice(0, 2).join(':')}
        </div>
      `;
    }).join('');
    
    if (reservation.date === date) {
      saveBtn.disabled = false;
      saveBtn.dataset.selectedTimeId = reservation.time.id;
    }
  } catch (error) {
    container.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
  }
};

let selectedAdminTimeId = null;
window.selectAdminEditTime = (resId, timeId) => {
  selectedAdminTimeId = timeId;
  const panel = document.getElementById(`admin-edit-panel-${resId}`);
  panel.querySelectorAll('.time-slot').forEach(el => {
    el.classList.toggle('selected', Number(el.dataset.id) === timeId);
  });
  const saveBtn = document.getElementById(`admin-save-btn-${resId}`);
  saveBtn.disabled = false;
  saveBtn.dataset.selectedTimeId = timeId;
};

window.saveAdminReservationUpdate = async (id) => {
  const reservation = allReservations.find(r => r.id === id);
  const date = document.getElementById(`admin-edit-date-${id}`).value;
  const timeId = document.getElementById(`admin-save-btn-${id}`).dataset.selectedTimeId;

  try {
    await api.updateReservation(id, { date, timeId }, reservation.name);
    alert('예약이 수정되었습니다.');
    loadReservations();
  } catch (error) {
    alert(error.message);
  }
};

document.getElementById('theme-name').addEventListener('input', (event) => {
  const thumbnailInput = document.getElementById('theme-thumbnail');
  thumbnailInput.placeholder = getNextThemeThumbnail(event.target.value || '방탈출');
});

document.getElementById('add-theme-btn').addEventListener('click', async () => {
  const name = document.getElementById('theme-name').value.trim();
  const description = document.getElementById('theme-description').value.trim();
  const thumbnail = document.getElementById('theme-thumbnail').value.trim() || getNextThemeThumbnail(name);

  if (!name || !description) {
    alert('테마 이름과 설명을 입력해주세요.');
    return;
  }

  try {
    await api.createTheme({ name, description, thumbnail });
    document.getElementById('theme-name').value = '';
    document.getElementById('theme-description').value = '';
    document.getElementById('theme-thumbnail').value = '';
    loadThemes();
  } catch (error) {
    alert(error.message);
  }
});

// --- Android-style Time Picker Logic ---
const timeOverlay = document.getElementById('time-overlay');
const openTimeOverlayBtn = document.getElementById('open-time-overlay');
const closeTimeOverlayBtn = document.getElementById('close-time-overlay');
const confirmTimeBtn = document.getElementById('confirm-time-btn');
const clockFace = document.getElementById('clock-face');
const clockHand = document.getElementById('clock-hand');
const displayHour = document.getElementById('display-hour');
const displayMinute = document.getElementById('display-minute');
const btnModeHour = document.getElementById('mode-hour');
const btnModeMinute = document.getElementById('mode-minute');

let pickerMode = 'hour'; // 'hour' or 'minute'
let currentHour = 12;
let currentMinute = 0;

const openTimeOverlay = () => {
  timeOverlay.style.display = 'flex';
  document.body.style.overflow = 'hidden';
  setPickerMode('hour');
  updateDisplay();
};

const closeTimeOverlay = () => {
  timeOverlay.style.display = 'none';
  document.body.style.overflow = 'auto';
};

const setPickerMode = (mode) => {
  pickerMode = mode;
  btnModeHour.classList.toggle('active', mode === 'hour');
  btnModeMinute.classList.toggle('active', mode === 'minute');
  displayHour.classList.toggle('active', mode === 'hour');
  displayMinute.classList.toggle('active', mode === 'minute');
  renderClockFace();
  updateHand();
};

const renderClockFace = () => {
  // Clear existing numbers except hand and center
  const existingNumbers = clockFace.querySelectorAll('.clock-number');
  existingNumbers.forEach(n => n.remove());

  const radius = 100;
  const count = pickerMode === 'hour' ? 12 : 12; // 12 intervals for both
  const step = 360 / count;

  for (let i = 1; i <= count; i++) {
    const value = pickerMode === 'hour' ? i : (i === 12 ? 0 : i * 5);
    const angle = (i * step) - 90;
    const x = Math.cos(angle * Math.PI / 180) * radius;
    const y = Math.sin(angle * Math.PI / 180) * radius;

    const el = document.createElement('div');
    el.className = 'clock-number';
    el.textContent = pickerMode === 'minute' ? String(value).padStart(2, '0') : value;
    el.style.left = `calc(50% + ${x}px - 16px)`;
    el.style.top = `calc(50% + ${y}px - 16px)`;
    
    el.onclick = () => selectValue(value);
    clockFace.appendChild(el);
  }
};

const selectValue = (value) => {
  if (pickerMode === 'hour') {
    currentHour = value;
    setPickerMode('minute');
  } else {
    currentMinute = value;
  }
  updateDisplay();
  updateHand();
};

const updateDisplay = () => {
  displayHour.textContent = String(currentHour).padStart(2, '0');
  displayMinute.textContent = String(currentMinute).padStart(2, '0');
};

const updateHand = () => {
  let angle = 0;
  if (pickerMode === 'hour') {
    angle = (currentHour % 12) * 30;
  } else {
    angle = (currentMinute / 60) * 360;
  }
  clockHand.style.transform = `translateX(-50%) rotate(${angle}deg)`;
};

btnModeHour.onclick = () => setPickerMode('hour');
btnModeMinute.onclick = () => setPickerMode('minute');
displayHour.onclick = () => setPickerMode('hour');
displayMinute.onclick = () => setPickerMode('minute');

openTimeOverlayBtn.addEventListener('click', openTimeOverlay);
closeTimeOverlayBtn.addEventListener('click', closeTimeOverlay);

confirmTimeBtn.addEventListener('click', async () => {
  const timeStr = `${String(currentHour).padStart(2, '0')}:${String(currentMinute).padStart(2, '0')}`;
  
  confirmTimeBtn.disabled = true;
  confirmTimeBtn.textContent = '등록 중...';

  try {
    await api.createTime(timeStr);
    closeTimeOverlay();
    loadTimes();
  } catch (error) {
    alert(error.message);
  } finally {
    confirmTimeBtn.disabled = false;
    confirmTimeBtn.textContent = '시스템에 시간 등록';
  }
});

document.addEventListener('click', async (e) => {
  if (e.target.classList.contains('btn-delete')) {
    const id = e.target.dataset.id;
    const type = e.target.dataset.type;
    const typeName = {
      theme: '테마',
      time: '시간',
      reservation: '예약'
    }[type] || '항목';

    if (confirm(`이 ${typeName}을 삭제할까요?`)) {
      try {
        if (type === 'theme') {
          await api.deleteTheme(id);
          loadThemes();
        } else if (type === 'reservation') {
          await api.deleteReservation(id);
          loadReservations();
        } else {
          await api.deleteTime(id);
          loadTimes();
        }
      } catch (error) {
        alert(error.message);
      }
    }
  }

  if (e.target.classList.contains('btn-edit-res')) {
    const id = Number(e.target.dataset.id);
    const panel = document.getElementById(`admin-edit-panel-${id}`);
    const isOpening = panel.style.display !== 'block';

    document.querySelectorAll('.edit-panel').forEach(p => p.style.display = 'none');

    if (isOpening) {
      panel.style.display = 'block';
      loadAdminEditSlots(id);
    }
  }
});

loadThemes();
loadTimes();
loadReservations();
