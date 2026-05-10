import { api, getNextThemeThumbnail, getThemeThumbnail } from './api.js';

const themeList = document.getElementById('theme-list');
const timeList = document.getElementById('time-list');
const reservationList = document.getElementById('reservation-list');

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

    timeList.innerHTML = times.map(time => `
      <div class="item">
        <div class="item-info">
          <span class="item-name">${escapeHtml(time.startAt)}</span>
        </div>
        <button class="btn-delete" data-id="${time.id}" data-type="time">삭제</button>
      </div>
    `).join('');
  } catch (error) {
    timeList.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
  }
};

const loadReservations = async () => {
  try {
    const reservations = await api.getReservations();

    if (reservations.length === 0) {
      reservationList.innerHTML = '<div class="item muted">예약 내역이 없습니다.</div>';
      return;
    }

    reservationList.innerHTML = reservations.map(reservation => `
      <div class="item">
        <div class="item-info">
          <span class="item-name">${escapeHtml(reservation.name)} · ${escapeHtml(reservation.theme?.name)}</span>
          <span class="item-meta">${escapeHtml(reservation.date)} ${escapeHtml(reservation.time?.startAt)}</span>
        </div>
        <button class="btn-delete" data-id="${reservation.id}" data-type="reservation">삭제</button>
      </div>
    `).join('');
  } catch (error) {
    reservationList.innerHTML = `<div class="item muted">${escapeHtml(error.message)}</div>`;
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

document.getElementById('add-time-btn').addEventListener('click', async () => {
  const startAt = document.getElementById('time-start').value;

  if (!startAt) {
    alert('추가할 시간을 선택해주세요.');
    return;
  }

  try {
    await api.createTime(startAt);
    document.getElementById('time-start').value = '';
    loadTimes();
  } catch (error) {
    alert(error.message);
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
});

loadThemes();
loadTimes();
loadReservations();
