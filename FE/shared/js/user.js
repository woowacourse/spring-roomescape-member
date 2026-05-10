import { api, getThemeThumbnail } from './api.js';

const popularGrid = document.getElementById('popular-theme-grid');
const themeGrid = document.getElementById('theme-grid');
const bookingOverlay = document.getElementById('booking-overlay');
const closeOverlayBtn = document.getElementById('close-overlay');
const dateInput = document.getElementById('reservation-date');
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

const renderThemeCard = (theme, index, className = 'theme-card') => {
  const thumbnail = getThemeThumbnail(theme, index);
  return `
    <div class="${className}" data-id="${theme.id}">
      <div class="theme-image" style="background-image: linear-gradient(135deg, rgba(17, 17, 17, 0.08), rgba(17, 17, 17, 0.34)), url('${escapeCssUrl(thumbnail)}')"></div>
      <div class="theme-content">
        <h3 class="theme-title">${escapeHtml(theme.name)}</h3>
        <p class="theme-description">${escapeHtml(theme.description)}</p>
      </div>
    </div>
  `;
};

// --- Navigation ---
const goToStep = (stepNumber) => {
  document.querySelectorAll('.step-pane').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.step').forEach(s => s.classList.remove('active'));
  
  document.getElementById(`step-${stepNumber}-content`).classList.add('active');
  document.querySelector(`.step[data-step="${stepNumber}"]`).classList.add('active');
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
  themeGrid.innerHTML = '<p class="empty-state">테마를 불러오는 중입니다...</p>';

  try {
    const themes = await api.getThemes();
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
    themeGrid.innerHTML = '<p class="empty-state">테마를 불러오지 못했습니다.</p>';
    popularGrid.innerHTML = '<p class="empty-state">인기 테마를 불러오지 못했습니다.</p>';
  }
};

const loadPopularThemes = async (fallbackThemes = []) => {
  popularGrid.innerHTML = '<p class="empty-state">인기 테마를 불러오는 중입니다...</p>';

  try {
    const range = getMonthRange();
    const popularThemes = await api.getPopularThemes({ ...range, limit: 3 });
    const themes = popularThemes.length > 0 ? popularThemes : fallbackThemes.slice(0, 3);

    if (themes.length === 0) {
      popularGrid.innerHTML = '<p class="empty-state">아직 인기 테마 집계가 없습니다.</p>';
      return;
    }

    popularGrid.innerHTML = themes.map((theme, index) => renderThemeCard(theme, index, 'popular-card')).join('');
  } catch (error) {
    const themes = fallbackThemes.slice(0, 3);
    popularGrid.innerHTML = themes.length > 0
      ? themes.map((theme, index) => renderThemeCard(theme, index, 'popular-card')).join('')
      : '<p class="empty-state">인기 테마를 불러오지 못했습니다.</p>';
  }
};

const updateTimes = async () => {
  if (!selectedTheme || !dateInput.value) return;

  nextBtn.disabled = true;
  timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: #8e8e93;">예약 가능 시간을 확인하는 중입니다...</p>';

  try {
    const times = await api.getReservableTimes(dateInput.value, selectedTheme.id);
    if (times.length === 0) {
      timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center;">선택한 날짜에 등록된 시간이 없습니다.</p>';
      return;
    }
    
    timeSlotsContainer.innerHTML = times.map(time => `
      <div class="time-slot ${time.available ? '' : 'disabled'}" 
           data-id="${time.timeId}" 
           data-available="${time.available}">
        ${time.startAt}
      </div>
    `).join('');
  } catch (error) {
    timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; color: #ff3b30;">예약 가능 시간을 불러오지 못했습니다.</p>';
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

dateInput.addEventListener('change', updateTimes);

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

// Initial load
loadThemes();
