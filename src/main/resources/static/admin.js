document.addEventListener('DOMContentLoaded', () => {
  const themeList = document.getElementById('theme-list');
  const timeList = document.getElementById('time-list');
  const reservationList = document.getElementById('reservation-list');
  const filterThemeSelect = document.getElementById('filter-theme');
  const filterTimeSelect = document.getElementById('filter-time');
  
  let allReservations = []; // To store reservations for filtering

  loadThemes();
  loadTimes();
  loadReservations();

  function loadThemes() {
    fetch('/themes').then(r => r.json()).then(data => {
      themeList.innerHTML = '';
      filterThemeSelect.innerHTML = '<option value="">모든 테마</option>';
      data.forEach(t => {
        themeList.innerHTML += `<tr>
          <td>${t.id}</td><td>${t.name}</td><td>${t.description}</td>
          <td><button class="btn-danger" onclick="deleteTheme(${t.id})">삭제</button></td>
        </tr>`;
        filterThemeSelect.innerHTML += `<option value="${t.id}">${t.name}</option>`;
      });
    });
  }

  function loadTimes() {
    fetch('/admin/times').then(r => r.json()).then(data => {
      timeList.innerHTML = '';
      filterTimeSelect.innerHTML = '<option value="">모든 시간</option>';
      data.forEach(t => {
        timeList.innerHTML += `<tr>
          <td>${t.id}</td><td>${t.startAt}</td>
          <td><button class="btn-danger" onclick="deleteTime(${t.id})">삭제</button></td>
        </tr>`;
        filterTimeSelect.innerHTML += `<option value="${t.id}">${t.startAt}</option>`;
      });
    });
  }

  function loadReservations() {
    fetch('/reservations').then(r => r.json()).then(data => {
      allReservations = data.reservations;
      renderReservations();
    });
  }

  function renderReservations() {
    const fName = document.getElementById('filter-name').value.toLowerCase();
    const fDate = document.getElementById('filter-date').value;
    const fTheme = document.getElementById('filter-theme').value;
    const fTime = document.getElementById('filter-time').value;

    const filtered = allReservations.filter(r => {
      if (fName && !r.name.toLowerCase().includes(fName)) return false;
      if (fDate && r.date !== fDate) return false;
      if (fTheme && r.theme && r.theme.id.toString() !== fTheme) return false;
      if (fTime && r.time && r.time.id.toString() !== fTime) return false;
      return true;
    });

    reservationList.innerHTML = '';
    if (filtered.length === 0) {
      reservationList.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px;">검색 결과가 없습니다.</td></tr>';
      return;
    }
    
    filtered.forEach(r => {
      reservationList.innerHTML += `<tr>
        <td>${r.id}</td><td>${r.name}</td><td>${r.date}</td>
        <td>${r.time ? r.time.startAt : ''}</td><td>${r.theme ? r.theme.name : ''}</td>
        <td><button class="btn-danger" onclick="deleteRes(${r.id})">취소</button></td>
      </tr>`;
    });
  }

  // Bind filter events
  ['filter-name', 'filter-date', 'filter-theme', 'filter-time'].forEach(id => {
    document.getElementById(id).addEventListener('input', renderReservations);
  });

  document.getElementById('theme-form').onsubmit = async (e) => {
    e.preventDefault();
    const res = await fetch('/admin/themes', {
      method: 'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({
        name: document.getElementById('theme-name').value,
        description: document.getElementById('theme-desc').value,
        thumbnail: document.getElementById('theme-thumb').value
      })
    });
    if(!res.ok) alert(await res.text());
    else { e.target.reset(); loadThemes(); }
  };

  document.getElementById('time-form').onsubmit = async (e) => {
    e.preventDefault();
    const res = await fetch('/admin/times', {
      method: 'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({startAt: document.getElementById('time-val').value})
    });
    if(!res.ok) alert(await res.text());
    else { e.target.reset(); loadTimes(); }
  };

  window.deleteTheme = async (id) => {
    if(!confirm('해당 테마를 삭제하시겠습니까?')) return;
    const res = await fetch(`/admin/themes/${id}`, {method: 'DELETE'});
    if(!res.ok) alert(await res.text());
    else loadThemes();
  };

  window.deleteTime = async (id) => {
    if(!confirm('해당 시간을 삭제하시겠습니까?')) return;
    const res = await fetch(`/admin/times/${id}`, {method: 'DELETE'});
    if(!res.ok) alert(await res.text());
    else loadTimes();
  };

  window.deleteRes = async (id) => {
    if(!confirm('해당 예약을 취소하시겠습니까?')) return;
    const res = await fetch(`/admin/reservations/${id}`, {method: 'DELETE'});
    if(!res.ok) alert(await res.text());
    else loadReservations();
  };
});
