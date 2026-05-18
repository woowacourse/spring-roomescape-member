document.addEventListener('DOMContentLoaded', () => {
  const resThemeSelect = document.getElementById('res-theme');
  const resDateInput = document.getElementById('res-date');
  const resTimeSelect = document.getElementById('res-time');
  const resForm = document.getElementById('reservation-form');
  const myReservationForm = document.getElementById('my-reservation-form');
  const myReservationNameInput = document.getElementById('my-reservation-name');
  const myReservationList = document.getElementById('my-reservation-list');
  const myReservationMessage = document.getElementById('my-reservation-message');
  let myReservations = [];

  document.querySelectorAll('.page-tab').forEach(tab => {
    tab.addEventListener('click', () => {
      const targetId = tab.dataset.tabTarget;
      document.querySelectorAll('.page-tab').forEach(item => item.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(panel => panel.classList.remove('active'));
      tab.classList.add('active');
      document.getElementById(targetId).classList.add('active');
    });
  });

  // Prevent past dates
  const today = new Date().toISOString().split('T')[0];
  resDateInput.min = today;

  async function getErrorMessage(res) {
    const fallbackMessage = '요청을 처리할 수 없습니다. 잠시 후 다시 시도해주세요.';

    try {
      const error = await res.clone().json();
      return error.action || error.message || fallbackMessage;
    } catch (e) {
      try {
        const text = await res.text();
        return text || fallbackMessage;
      } catch (e) {
        return fallbackMessage;
      }
    }
  }

  function showMyReservationMessage(message, type = 'info') {
    myReservationMessage.textContent = message;
    myReservationMessage.className = `status-message ${type}`;
    myReservationMessage.style.display = 'block';
  }

  function hideMyReservationMessage() {
    myReservationMessage.textContent = '';
    myReservationMessage.style.display = 'none';
  }

  // Get themeId from URL
  const urlParams = new URLSearchParams(window.location.search);
  const initialThemeId = urlParams.get('themeId');

  // Load Themes into Select
  fetch('/themes')
    .then(res => res.json())
    .then(themes => {
      resThemeSelect.innerHTML = '<option value="">테마를 선택하세요</option>';
      themes.forEach(t => {
        const isSelected = initialThemeId && t.id.toString() === initialThemeId ? 'selected' : '';
        resThemeSelect.innerHTML += `<option value="${t.id}" ${isSelected}>${t.name}</option>`;
      });
      // If initialThemeId exists, trigger checkAvailableTimes if date is also selected
      if(initialThemeId && resDateInput.value) {
        checkAvailableTimes();
      }
    });

  function checkAvailableTimes() {
    const themeId = resThemeSelect.value;
    const date = resDateInput.value;
    if(!themeId || !date) {
      resTimeSelect.innerHTML = '<option value="">날짜와 테마를 먼저 선택하세요</option>';
      resTimeSelect.disabled = true;
      return;
    }

    fetch(`/themes/${themeId}/reservation-times?date=${date}`)
      .then(res => res.json())
      .then(times => {
        resTimeSelect.innerHTML = '<option value="">시간을 선택하세요</option>';
        let hasAvailable = false;
        times.forEach(t => {
          if(t.available) {
            hasAvailable = true;
            resTimeSelect.innerHTML += `<option value="${t.id}">${t.startAt}</option>`;
          }
        });
        if(!hasAvailable) {
          resTimeSelect.innerHTML = '<option value="">해당 날짜에 예약 가능한 시간이 없습니다.</option>';
          resTimeSelect.disabled = true;
        } else {
          resTimeSelect.disabled = false;
        }
      });
  }

  resThemeSelect.addEventListener('change', checkAvailableTimes);
  resDateInput.addEventListener('change', checkAvailableTimes);

  resForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      name: document.getElementById('res-name').value,
      date: resDateInput.value,
      themeId: resThemeSelect.value,
      timeId: resTimeSelect.value
    };

    fetch('/reservations', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    }).then(async res => {
      if(res.ok) {
        alert('성공적으로 예약이 완료되었습니다.');
        checkAvailableTimes();
        document.getElementById('res-name').value = '';
        if (myReservationNameInput.value === payload.name) {
          loadMyReservations(payload.name);
        }
      } else {
        const msg = await getErrorMessage(res);
        alert(msg);
      }
    });
  });

  async function loadMyReservations(name) {
    const trimmedName = name.trim();
    if (!trimmedName) {
      showMyReservationMessage('예약자 이름을 입력해주세요.', 'error');
      return;
    }

    try {
      const res = await fetch(`/reservations?name=${encodeURIComponent(trimmedName)}`);
      if (!res.ok) {
        showMyReservationMessage(await getErrorMessage(res), 'error');
        return;
      }

      const data = await res.json();
      myReservations = data.reservations || [];
      renderMyReservations();
    } catch (e) {
      showMyReservationMessage('예약 목록을 불러오지 못했습니다. 잠시 후 다시 시도해주세요.', 'error');
    }
  }

  function renderMyReservations() {
    hideMyReservationMessage();
    myReservationList.innerHTML = '';

    if (myReservations.length === 0) {
      myReservationList.innerHTML = '<tr><td colspan="4" class="table-empty">예약이 없습니다.</td></tr>';
      showMyReservationMessage('조회된 예약이 없습니다.', 'info');
      return;
    }

    myReservations.forEach(reservation => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>
          <strong>${reservation.theme.name}</strong>
          <div class="table-muted">예약번호 ${reservation.id}</div>
        </td>
        <td>
          <input type="date" class="form-control table-control" value="${reservation.date}" min="${today}" data-update-date="${reservation.id}">
        </td>
        <td>
          <select class="form-control table-control" data-update-time="${reservation.id}">
            <option value="${reservation.time.id}">${reservation.time.startAt}</option>
          </select>
        </td>
        <td>
          <div class="row-actions">
            <button type="button" class="btn-secondary btn-small" data-action="load-times" data-id="${reservation.id}">시간 조회</button>
            <button type="button" class="btn-primary btn-small" data-action="update" data-id="${reservation.id}">변경</button>
            <button type="button" class="btn-danger btn-small" data-action="cancel" data-id="${reservation.id}">취소</button>
          </div>
        </td>
      `;
      myReservationList.appendChild(row);
    });
  }

  async function loadAvailableTimesForReservation(id) {
    const reservation = myReservations.find(item => item.id === id);
    const dateInput = document.querySelector(`[data-update-date="${id}"]`);
    const timeSelect = document.querySelector(`[data-update-time="${id}"]`);
    const date = dateInput.value;

    if (!reservation || !date) {
      showMyReservationMessage('변경할 날짜를 선택해주세요.', 'error');
      return;
    }

    try {
      const res = await fetch(`/themes/${reservation.theme.id}/reservation-times?date=${date}`);
      if (!res.ok) {
        showMyReservationMessage(await getErrorMessage(res), 'error');
        return;
      }

      const times = await res.json();
      const availableTimes = times.filter(time => time.available);
      timeSelect.innerHTML = '';

      if (availableTimes.length === 0) {
        timeSelect.innerHTML = '<option value="">예약 가능한 시간이 없습니다</option>';
        showMyReservationMessage('선택한 날짜에 예약 가능한 시간이 없습니다.', 'info');
        return;
      }

      availableTimes.forEach(time => {
        timeSelect.innerHTML += `<option value="${time.id}">${time.startAt}</option>`;
      });
      hideMyReservationMessage();
    } catch (e) {
      showMyReservationMessage('예약 가능한 시간을 불러오지 못했습니다.', 'error');
    }
  }

  async function updateMyReservation(id) {
    const date = document.querySelector(`[data-update-date="${id}"]`).value;
    const timeId = document.querySelector(`[data-update-time="${id}"]`).value;

    if (!date || !timeId) {
      showMyReservationMessage('변경할 날짜와 시간을 선택해주세요.', 'error');
      return;
    }

    const res = await fetch(`/reservations/${id}`, {
      method: 'PATCH',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({date, timeId})
    });

    if (!res.ok) {
      showMyReservationMessage(await getErrorMessage(res), 'error');
      return;
    }

    await loadMyReservations(myReservationNameInput.value);
    showMyReservationMessage('예약이 변경되었습니다.', 'success');
  }

  async function cancelMyReservation(id) {
    if (!confirm('이 예약을 취소하시겠습니까?')) {
      return;
    }

    const res = await fetch(`/reservations/${id}`, {method: 'DELETE'});
    if (!res.ok) {
      showMyReservationMessage(await getErrorMessage(res), 'error');
      return;
    }

    await loadMyReservations(myReservationNameInput.value);
    showMyReservationMessage('예약이 취소되었습니다.', 'success');
    checkAvailableTimes();
  }

  myReservationForm.addEventListener('submit', (e) => {
    e.preventDefault();
    loadMyReservations(myReservationNameInput.value);
  });

  myReservationList.addEventListener('change', (e) => {
    const id = Number(e.target.dataset.updateDate);
    if (id) {
      loadAvailableTimesForReservation(id);
    }
  });

  myReservationList.addEventListener('click', (e) => {
    const button = e.target.closest('button[data-action]');
    if (!button) {
      return;
    }

    const id = Number(button.dataset.id);
    if (button.dataset.action === 'load-times') {
      loadAvailableTimesForReservation(id);
    }
    if (button.dataset.action === 'update') {
      updateMyReservation(id);
    }
    if (button.dataset.action === 'cancel') {
      cancelMyReservation(id);
    }
  });
});
