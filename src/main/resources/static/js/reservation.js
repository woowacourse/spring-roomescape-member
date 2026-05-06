const RESERVATION_API = '/reservations';
const THEME_API = '/admin/theme';

let selectedDate = null;
let selectedThemeId = null;

document.addEventListener('DOMContentLoaded', () => {
  loadThemes();
  document.getElementById('search-btn').addEventListener('click', searchAvailableTimes);
});

function loadThemes() {
  fetch(THEME_API)
    .then(res => res.json())
    .then(themes => {
      const select = document.getElementById('filter-theme');
      themes.forEach(theme => {
        const option = document.createElement('option');
        option.value = theme.id;
        option.textContent = theme.name;
        select.appendChild(option);
      });
    });
}

function searchAvailableTimes() {
  const date = document.getElementById('filter-date').value;
  const themeId = document.getElementById('filter-theme').value;

  if (!date || !themeId) {
    alert('날짜와 테마를 선택해주세요.');
    return;
  }

  selectedDate = date;
  selectedThemeId = themeId;

  fetch(`${RESERVATION_API}?date=${date}&themeId=${themeId}`)
    .then(res => res.json())
    .then(renderAvailableTimes);
}

function renderAvailableTimes(times) {
  const tableBody = document.getElementById('reservation-table-body');
  const resultSection = document.getElementById('result-section');
  const noTimesMsg = document.getElementById('no-times-msg');

  tableBody.innerHTML = '';
  resultSection.classList.remove('d-none');

  if (times.length === 0) {
    noTimesMsg.classList.remove('d-none');
    return;
  }

  noTimesMsg.classList.add('d-none');
  times.forEach(time => {
    const row = tableBody.insertRow();
    row.dataset.timeId = time.id;
    row.insertCell(0).textContent = time.startAt;
    const actionCell = row.insertCell(1);
    actionCell.appendChild(createButton('예약하기', 'btn-primary', showBookingForm));
  });
}

function showBookingForm(event) {
  const row = event.target.closest('tr');
  const actionCell = row.cells[1];
  actionCell.innerHTML = '';

  const nameInput = document.createElement('input');
  nameInput.type = 'text';
  nameInput.placeholder = '예약자 이름';
  nameInput.className = 'form-control d-inline-block mr-2';
  nameInput.style.width = '140px';

  actionCell.appendChild(nameInput);
  actionCell.appendChild(createButton('확인', 'btn-success mr-1', () => confirmBooking(row, nameInput)));
  actionCell.appendChild(createButton('취소', 'btn-secondary', () => cancelBooking(row)));
}

function confirmBooking(row, nameInput) {
  const name = nameInput.value.trim();
  if (!name) {
    alert('예약자 이름을 입력해주세요.');
    return;
  }

  const body = {
    name,
    date: selectedDate,
    timeId: parseInt(row.dataset.timeId),
    themeId: parseInt(selectedThemeId)
  };

  fetch(RESERVATION_API, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(body)
  })
    .then(res => {
      if (res.status === 200) return res.json();
      throw new Error('예약 실패');
    })
    .then(() => row.remove())
    .catch(err => alert(err.message));
}

function cancelBooking(row) {
  const actionCell = row.cells[1];
  actionCell.innerHTML = '';
  actionCell.appendChild(createButton('예약하기', 'btn-primary', showBookingForm));
}

function createButton(label, className, handler) {
  const btn = document.createElement('button');
  btn.textContent = label;
  btn.className = `btn ${className}`;
  btn.addEventListener('click', handler);
  return btn;
}