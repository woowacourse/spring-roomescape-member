document.addEventListener('DOMContentLoaded', () => {
  // Elements
  const themeList = document.getElementById('theme-list');
  const themeForm = document.getElementById('theme-form');
  const timeList = document.getElementById('time-list');
  const timeForm = document.getElementById('time-form');
  const reservationList = document.getElementById('reservation-list');

  // Load Initial Data
  loadThemes();
  loadTimes();
  loadReservations();

  // --- THEME MANAGEMENT ---
  function loadThemes() {
    fetch('/themes')
      .then(res => res.json())
      .then(data => {
        themeList.innerHTML = '';
        data.forEach(theme => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${theme.id}</td>
            <td><strong>${theme.name}</strong></td>
            <td>${theme.description}</td>
            <td><button class="btn-danger" onclick="deleteTheme(${theme.id})">삭제</button></td>
          `;
          themeList.appendChild(tr);
        });
      });
  }

  themeForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      name: document.getElementById('theme-name').value,
      description: document.getElementById('theme-desc').value,
      thumbnail: document.getElementById('theme-thumb').value
    };

    fetch('/admin/themes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    }).then(res => {
      if(res.ok) {
        document.getElementById('theme-name').value = '';
        document.getElementById('theme-desc').value = '';
        document.getElementById('theme-thumb').value = '';
        loadThemes();
      } else {
        alert('테마 추가 실패');
      }
    });
  });

  window.deleteTheme = function(id) {
    if(!confirm('정말 삭제하시겠습니까? 관련 예약이 있으면 삭제되지 않을 수 있습니다.')) return;
    fetch(`/admin/themes/${id}`, { method: 'DELETE' })
      .then(() => loadThemes())
      .catch(() => alert('삭제 실패'));
  };

  // --- TIME MANAGEMENT ---
  function loadTimes() {
    fetch('/admin/times')
      .then(res => res.json())
      .then(data => {
        timeList.innerHTML = '';
        data.forEach(time => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${time.id}</td>
            <td><strong>${time.startAt}</strong></td>
            <td><button class="btn-danger" onclick="deleteTime(${time.id})">삭제</button></td>
          `;
          timeList.appendChild(tr);
        });
      });
  }

  timeForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
      startAt: document.getElementById('time-val').value
    };

    fetch('/admin/times', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    }).then(res => {
      if(res.ok) {
        document.getElementById('time-val').value = '';
        loadTimes();
      } else {
        alert('시간 추가 실패');
      }
    });
  });

  window.deleteTime = function(id) {
    if(!confirm('정말 삭제하시겠습니까? 관련 예약이 있으면 삭제되지 않을 수 있습니다.')) return;
    fetch(`/admin/times/${id}`, { method: 'DELETE' })
      .then(() => loadTimes())
      .catch(() => alert('삭제 실패'));
  };

  // --- RESERVATION MANAGEMENT ---
  function loadReservations() {
    fetch('/reservations')
      .then(res => res.json())
      .then(data => {
        reservationList.innerHTML = '';
        data.forEach(res => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${res.id}</td>
            <td><strong>${res.name}</strong></td>
            <td>${res.date}</td>
            <td>${res.time ? res.time.startAt : ''}</td>
            <td>${res.theme ? res.theme.name : ''}</td>
            <td><button class="btn-danger" onclick="deleteReservation(${res.id})">취소</button></td>
          `;
          reservationList.appendChild(tr);
        });
      });
  }

  window.deleteReservation = function(id) {
    if(!confirm('정말 예약을 취소하시겠습니까?')) return;
    fetch(`/admin/reservations/${id}`, { method: 'DELETE' })
      .then(() => loadReservations())
      .catch(() => alert('취소 실패'));
  };
});
