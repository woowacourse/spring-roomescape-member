import { api, getThemeThumbnail } from './api.js';
import { ui } from './ui.js';

// --- Matrix Rain Effect ---
const initMatrix = () => {
  const canvas = document.getElementById('matrix-canvas');
  if (!canvas) return;
  
  const ctx = canvas.getContext('2d');
  let width = canvas.width = window.innerWidth;
  let height = canvas.height = window.innerHeight;

  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$+-*/=%' + 'ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ';
  const fontSize = 16;
  const columns = Math.floor(width / fontSize);
  const drops = Array(columns).fill(1);

  const draw = () => {
    ctx.fillStyle = 'rgba(0, 0, 0, 0.05)';
    ctx.fillRect(0, 0, width, height);

    ctx.fillStyle = '#2AC1BC'; // Baemin Mint Matrix
    ctx.font = `${fontSize}px monospace`;

    for (let i = 0; i < drops.length; i++) {
      const text = characters.charAt(Math.floor(Math.random() * characters.length));
      ctx.fillText(text, i * fontSize, drops[i] * fontSize);

      if (drops[i] * fontSize > height && Math.random() > 0.975) {
        drops[i] = 0;
      }
      drops[i]++;
    }
  };

  let interval = setInterval(draw, 33);

  window.addEventListener('resize', () => {
    width = canvas.width = window.innerWidth;
    height = canvas.height = window.innerHeight;
    const newColumns = Math.floor(width / fontSize);
    if (newColumns > drops.length) {
      drops.push(...Array(newColumns - drops.length).fill(1));
    }
  });
};

const popularGrid = document.getElementById('popular-theme-grid');
const themeGrid = document.getElementById('theme-grid');
const bookingOverlay = document.getElementById('booking-overlay');
const closeOverlayBtn = document.getElementById('close-overlay');
const dateInput = document.getElementById('reservation-date');
const datePickerTrigger = document.getElementById('date-picker-trigger');
const dateDisplayText = document.getElementById('selected-date-text');
const timeSlotsContainer = document.getElementById('time-slots');
const nextBtn = document.getElementById('next-to-step-2');
const reserveBtn = document.getElementById('reserve-btn');
const finishBtn = document.getElementById('finish-btn');

let selectedTime = null;
let selectedTheme = null;
let themesById = new Map();

const escapeHtml = (value) => String(value ?? '')
  .replaceAll('&', '&amp;')
  .replaceAll('<', '&lt;')
  .replaceAll('>', '&gt;')
  .replaceAll('"', '&quot;')
  .replaceAll("'", '&#039;');

const escapeCssUrl = (value) => String(value ?? '').replaceAll("'", '%27');

const getMonthRange = () => {
  const now = new Date();
  const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
  const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);

  return {
    from: firstDay.toISOString().split('T')[0],
    to: lastDay.toISOString().split('T')[0]
  };
};

const renderThemeCard = (theme, index, className = 'theme-card', rank = null) => {
  const thumbnail = getThemeThumbnail(theme, index);
  const rankAttr = rank ? `data-rank="${rank}"` : '';
  return `
    <div class="${className} slide-up" data-id="${theme.id}" ${rankAttr} style="animation-delay: ${index * 0.05}s">
      <div class="theme-image" style="background-image: linear-gradient(135deg, rgba(17, 17, 17, 0.04), rgba(17, 17, 17, 0.12)), url('${escapeCssUrl(thumbnail)}')"></div>
      <div class="theme-content">
        <h3 class="theme-title">${escapeHtml(theme.name)}</h3>
        <p class="theme-description">${escapeHtml(theme.description)}</p>
      </div>
    </div>
  `;
};

const renderSkeletons = (count, className) => {
  return Array(count).fill(0).map(() => `
    <div class="${className} skeleton" style="height: 300px;"></div>
  `).join('');
};

// --- Navigation ---
const goToStep = (stepNumber) => {
  const panes = document.querySelectorAll('.step-pane');
  const steps = document.querySelectorAll('.step');
  const stepper = document.getElementById('booking-stepper');
  const themeInfo = document.getElementById('booking-theme-info');
  
  panes.forEach(p => p.classList.remove('active'));
  steps.forEach((s, i) => {
    s.classList.remove('active', 'completed');
    if (i + 1 < stepNumber) s.classList.add('completed');
    if (i + 1 === stepNumber) s.classList.add('active');
  });

  // Step 3 is the final completion state - clean view without stepper/theme info
  if (stepNumber === 3) {
    if (stepper) stepper.style.display = 'none';
    if (themeInfo) themeInfo.style.display = 'none';
  } else {
    if (stepper) stepper.style.display = 'flex';
    if (themeInfo) themeInfo.style.display = 'block';
  }
  
  const targetPane = document.getElementById(`step-${stepNumber}-content`);
  targetPane.classList.add('active');
  
  // Smooth scroll to top of modal if content is long
  const modalContent = document.querySelector('.overlay-content');
  if (modalContent) modalContent.scrollTop = 0;
};

const openBooking = (theme) => {
  selectedTheme = theme;
  selectedTime = null;
  reserveBtn.disabled = false;
  reserveBtn.textContent = '예약하기';
  document.getElementById('selected-theme-name').textContent = theme.name;
  document.getElementById('selected-theme-desc').textContent = theme.description;
  
  // Set default date
  const today = new Date().toISOString().split('T')[0];
  dateInput.value = today;
  dateInput.min = today;
  
  bookingOverlay.style.display = 'flex';
  document.body.style.overflow = 'hidden';
  
  updateTimes();
  goToStep(1);
};

const closeBooking = () => {
  bookingOverlay.style.display = 'none';
  document.body.style.overflow = 'auto';
  selectedTime = null;
  selectedTheme = null;
  goToStep(1);
};

// --- Data Loading ---
const loadThemes = async () => {
  themeGrid.innerHTML = renderSkeletons(6, 'theme-card');
  popularGrid.innerHTML = renderSkeletons(3, 'popular-card');

  try {
    let themes = await api.getThemes();
    if (!Array.isArray(themes)) themes = [];
    
    themesById = new Map(themes.map(theme => [String(theme.id), theme]));
    await loadPopularThemes(themes);

    if (themes.length === 0) {
      themeGrid.innerHTML = `
        <div class="empty-state">
          <strong>등록된 테마가 없습니다.</strong>
          <span>관리자 화면에서 테마를 먼저 추가해주세요.</span>
        </div>
      `;
      return;
    }

    themeGrid.innerHTML = themes.map((theme, index) => renderThemeCard(theme, index)).join('');
  } catch (error) {
    themeGrid.innerHTML = `<p class="empty-state">테마를 불러오지 못했습니다. (${escapeHtml(error.message)})</p>`;
  }
};

const loadPopularThemes = async (fallbackThemes = []) => {
  try {
    const range = getMonthRange();
    const popularThemes = await api.getPopularThemes({ ...range, limit: 3 });
    const themes = popularThemes.length > 0 ? popularThemes : fallbackThemes.slice(0, 3);

    if (themes.length === 0) {
      popularGrid.innerHTML = '<p class="empty-state">아직 인기 테마 집계가 없습니다.</p>';
      return;
    }

    popularGrid.innerHTML = themes.map((theme, index) => renderThemeCard(theme, index, 'popular-card', index + 1)).join('');
  } catch (error) {
    const themes = fallbackThemes.slice(0, 3);
    popularGrid.innerHTML = themes.length > 0
      ? themes.map((theme, index) => renderThemeCard(theme, index, 'popular-card', index + 1)).join('')
      : '<p class="empty-state">인기 테마를 불러오지 못했습니다.</p>';
  }
};

const updateTimes = async () => {
  if (!selectedTheme || !dateInput.value) return;

  nextBtn.disabled = true;
  timeSlotsContainer.innerHTML = Array(6).fill(0).map(() => `<div class="time-slot skeleton"></div>`).join('');

  try {
    const times = await api.getReservableTimes(dateInput.value, selectedTheme.id);
    if (times.length === 0) {
      timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 20px; color: #8e8e93;">선택한 날짜에 등록된 시간이 없습니다.</p>';
      return;
    }
    
    timeSlotsContainer.innerHTML = times.map(time => {
      const displayTime = (time.startAt || '').split(':').slice(0, 2).join(':');
      return `
        <div class="time-slot ${time.available ? '' : 'disabled'} fade-in" 
             data-id="${time.timeId}" 
             data-available="${time.available}">
          ${displayTime}
        </div>
      `;
    }).join('');
  } catch (error) {
    timeSlotsContainer.innerHTML = `<p style="grid-column: 1/-1; color: #2AC1BC; text-align: center;">${escapeHtml(error.message)}</p>`;
  }
};

// --- Event Listeners ---
themeGrid.addEventListener('click', async (e) => {
  const card = e.target.closest('.theme-card');
  if (!card) return;

  const theme = themesById.get(String(card.dataset.id));
  if (!theme) return;

  openBooking(theme);
});

popularGrid.addEventListener('click', async (e) => {
  const card = e.target.closest('.popular-card');
  if (!card) return;

  const theme = themesById.get(String(card.dataset.id));
  if (!theme) return;

  openBooking(theme);
});

datePickerTrigger.addEventListener('click', async () => {
  const picked = await ui.pickDate(dateInput.value || new Date().toISOString().split('T')[0]);
  if (picked) {
    dateInput.value = picked;
    dateDisplayText.textContent = picked;
    updateTimes();
  }
});

timeSlotsContainer.addEventListener('click', (e) => {
  const slot = e.target.closest('.time-slot');
  if (!slot || slot.classList.contains('disabled')) return;

  document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
  slot.classList.add('selected');
  
  selectedTime = {
    id: slot.dataset.id,
    startAt: slot.textContent.trim()
  };
  
  nextBtn.disabled = false;
});

nextBtn.addEventListener('click', () => goToStep(2));
document.getElementById('back-to-step-1').addEventListener('click', () => goToStep(1));

reserveBtn.addEventListener('click', async () => {
  const name = document.getElementById('reservation-name').value;
  if (!name) {
    alert('예약자 이름을 입력해주세요.');
    return;
  }

  reserveBtn.disabled = true;
  reserveBtn.textContent = '예약 처리 중...';

  try {
    const reservation = {
      name,
      date: dateInput.value,
      timeId: selectedTime.id,
      themeId: selectedTheme.id
    };

    await api.createReservation(reservation);
    
    document.getElementById('reservation-summary').innerHTML = `
      <p><strong>테마:</strong> ${escapeHtml(selectedTheme.name)}</p>
      <p><strong>날짜:</strong> ${dateInput.value}</p>
      <p><strong>시간:</strong> ${selectedTime.startAt}</p>
      <p><strong>예약자:</strong> ${escapeHtml(name)}</p>
    `;
    
    goToStep(3);
  } catch (error) {
    alert(error.message || '예약에 실패했습니다. 다시 시도해주세요.');
    reserveBtn.disabled = false;
    reserveBtn.textContent = '예약하기';
  }
});

finishBtn.addEventListener('click', closeBooking);
closeOverlayBtn.addEventListener('click', () => {
  if (confirm('예약 창을 닫을까요? 입력한 내용은 저장되지 않습니다.')) closeBooking();
});

// --- Reservation Management ---
const resListContainer = document.getElementById('my-reservation-list');
const searchInput = document.getElementById('search-name');
const searchBtn = document.getElementById('search-btn');

const renderReservationCard = (res) => {
  const displayTime = (res.time?.startAt || '').split(':').slice(0, 2).join(':');
  return `
    <div class="res-card fade-in" data-id="${res.id}">
      <div class="res-header">
        <h3 class="res-theme-name">${escapeHtml(res.theme?.name)}</h3>
        <div class="res-status-badge">[ 예약 확정 ]</div>
      </div>
      <div class="res-info">
        <div class="res-item" data-label="날짜"><span>${res.date}</span></div>
        <div class="res-item" data-label="시간"><span>${displayTime}</span></div>
        <div class="res-item" data-label="예약자"><span>${escapeHtml(res.name)}</span></div>
      </div>
      <div class="res-actions">
        <button class="btn-cancel" onclick="cancelReservation(${res.id})">예약 취소</button>
        <button class="btn-edit" onclick="toggleEditPanel(${res.id})">예약 수정</button>
      </div>
      <div id="edit-panel-${res.id}" class="edit-panel">
        <div class="form-group">
          <label>변경할 날짜</label>
          <div id="edit-date-trigger-${res.id}" class="custom-date-display" onclick="pickEditDate(${res.id})">
            <span id="edit-date-text-${res.id}">${res.date}</span>
            <span class="icon">▼</span>
          </div>
          <input type="hidden" id="edit-date-${res.id}" value="${res.date}">
        </div>
        <div class="form-group">
          <label>변경할 시간</label>
          <div id="edit-slots-${res.id}" class="time-slots"></div>
        </div>
        <button class="btn-primary full-width glow-effect" onclick="saveReservationEdit(${res.id})" id="save-btn-${res.id}" disabled>변경사항 저장</button>
      </div>
    </div>
  `;
};

window.pickEditDate = async (id) => {
  const input = document.getElementById(`edit-date-${id}`);
  const text = document.getElementById(`edit-date-text-${id}`);
  const picked = await ui.pickDate(input.value);
  if (picked) {
    input.value = picked;
    text.textContent = picked;
    loadEditSlots(id);
  }
};

window.cancelReservation = async (id) => {
  const name = searchInput.value.trim();
  if (!confirm('예약을 취소하시겠습니까?')) return;

  try {
    await api.deleteReservationSelf(id, name);
    alert('예약이 취소되었습니다.');
    searchReservations();
  } catch (error) {
    alert(error.message);
  }
};

window.toggleEditPanel = async (id) => {
  const panel = document.getElementById(`edit-panel-${id}`);
  const isActive = panel.classList.contains('active');
  
  // Close all other panels
  document.querySelectorAll('.edit-panel').forEach(p => p.classList.remove('active'));
  
  if (!isActive) {
    panel.classList.add('active');
    const dateInput = document.getElementById(`edit-date-${id}`);
    
    // Initial slots load
    await loadEditSlots(id);
    
    // Use oninput for immediate feedback
    dateInput.oninput = () => loadEditSlots(id);
  }
};

const loadEditSlots = async (id) => {
  const dateInput = document.getElementById(`edit-date-${id}`);
  const slotsContainer = document.getElementById(`edit-slots-${id}`);
  const saveBtn = document.getElementById(`save-btn-${id}`);
  // Ensure ID type consistency (Number)
  const reservation = currentReservations.find(r => Number(r.id) === Number(id));
  
  if (!reservation) return;

  slotsContainer.innerHTML = Array(4).fill(0).map(() => '<div class="time-slot skeleton"></div>').join('');
  saveBtn.disabled = true;

  try {
    const times = await api.getReservableTimes(dateInput.value, reservation.theme.id);
    
    // Check if the current time slot is available on the selected date
    let autoSelectedId = null;

    slotsContainer.innerHTML = times.map(time => {
      const displayTime = (time.startAt || '').split(':').slice(0, 2).join(':');
      // If it's the current time of this reservation on the current date, it should be shown as available
      const isOriginalTime = String(reservation.time.id) === String(time.timeId);
      const isSameDate = reservation.date === dateInput.value;
      const available = time.available || (isSameDate && isOriginalTime);
      
      const isSelected = isOriginalTime;
      if (isSelected && available) autoSelectedId = time.timeId;

      return `
        <div class="time-slot ${available ? '' : 'disabled'} ${isSelected && available ? 'selected' : ''}" 
             data-id="${time.timeId}" 
             data-time="${displayTime}"
             onclick="selectEditSlot(${id}, this)">
          ${displayTime}
        </div>
      `;
    }).join('');

    if (autoSelectedId) {
      saveBtn.disabled = false;
      saveBtn.dataset.selectedTimeId = autoSelectedId;
    }
  } catch (error) {
    slotsContainer.innerHTML = `<p class="empty-state">시간을 불러오지 못했습니다: ${escapeHtml(error.message)}</p>`;
  }
};

window.selectEditSlot = (id, el) => {
  if (el.classList.contains('disabled')) return;
  
  const slotsContainer = document.getElementById(`edit-slots-${id}`);
  slotsContainer.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
  el.classList.add('selected');
  
  const saveBtn = document.getElementById(`save-btn-${id}`);
  saveBtn.disabled = false;
  saveBtn.dataset.selectedTimeId = el.dataset.id;
};

window.saveReservationEdit = async (id) => {
  const reservation = currentReservations.find(r => Number(r.id) === Number(id));
  if (!reservation) return;

  const date = document.getElementById(`edit-date-${id}`).value;
  const timeId = document.getElementById(`save-btn-${id}`).dataset.selectedTimeId;

  if (!date || !timeId) {
    alert('날짜와 시간을 선택해주세요.');
    return;
  }

  const saveBtn = document.getElementById(`save-btn-${id}`);
  const originalText = saveBtn.textContent;
  saveBtn.disabled = true;
  saveBtn.textContent = '처리 중...';

  try {
    // Use the name from the reservation object for reliability
    await api.updateReservation(id, { date, timeId }, reservation.name);
    alert('예약 정보가 성공적으로 수정되었습니다.');
    searchReservations();
  } catch (error) {
    alert(`수정 실패: ${error.message}`);
    saveBtn.disabled = false;
    saveBtn.textContent = originalText;
  }
};

let currentReservations = [];

const searchReservations = async () => {
  const name = searchInput.value.trim();
  if (!name) {
    alert('이름을 입력해주세요.');
    return;
  }

  resListContainer.innerHTML = renderSkeletons(3, 'res-card');
  
  try {
    currentReservations = await api.getReservations({ name });
    
    if (currentReservations.length === 0) {
      resListContainer.innerHTML = '<p class="empty-state">예약 내역이 없습니다.</p>';
      return;
    }

    resListContainer.innerHTML = currentReservations.map(res => renderReservationCard(res)).join('');
  } catch (error) {
    resListContainer.innerHTML = `<p class="empty-state">시스템 오류: 내역을 불러올 수 없습니다. (${escapeHtml(error.message)})</p>`;
  }
};

searchBtn.addEventListener('click', searchReservations);
searchInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') searchReservations();
});

// Initial load
loadThemes();
initMatrix();
