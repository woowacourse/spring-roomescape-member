document.addEventListener('DOMContentLoaded', () => {
  const resThemeSelect = document.getElementById('res-theme');
  const resDateInput = document.getElementById('res-date');
  const resTimeSelect = document.getElementById('res-time');
  const resForm = document.getElementById('reservation-form');

  // Prevent past dates
  const today = new Date().toISOString().split('T')[0];
  resDateInput.min = today;

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
        alert('성공적으로 예약이 완료되었습니다!');
        checkAvailableTimes();
        document.getElementById('res-name').value = '';
        // Optional: redirect to home
        window.location.href = '/';
      } else {
        const msg = await res.text();
        alert(msg);
      }
    });
  });
});
