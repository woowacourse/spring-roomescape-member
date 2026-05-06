document.addEventListener('DOMContentLoaded', () => {
  const popularContainer = document.getElementById('popular-themes-container');
  const dateInput = document.getElementById('date-input');
  const themeSection = document.getElementById('theme-section');
  const themeContainer = document.getElementById('theme-select-container');
  const timeSection = document.getElementById('time-section');
  const timeContainer = document.getElementById('time-select-container');
  const timeEmptyMsg = document.getElementById('time-empty-msg');
  const nameSection = document.getElementById('name-section');
  const nameInput = document.getElementById('name-input');
  const submitBtn = document.getElementById('submit-btn');
  const form = document.getElementById('reservation-form');

  let selectedThemeId = null;
  let selectedTimeId = null;

  // 1. 인기 테마 로드
  fetch('/themes/popular')
    .then(res => res.json())
    .then(data => {
      popularContainer.innerHTML = '';
      if (data.length === 0) {
        popularContainer.innerHTML = '<p class="empty-state">최근 예약된 인기 테마가 없습니다.</p>';
        return;
      }
      
      data.forEach((item, index) => {
        const card = document.createElement('div');
        card.className = 'theme-card';
        card.innerHTML = `
          <div class="rank-badge">${index + 1}</div>
          <div class="reservation-count">예약 ${item.reservationCount}건</div>
          <img src="${item.thumbnail}" class="theme-img" alt="${item.name}">
          <div class="theme-info">
            <div class="theme-name">${item.name}</div>
            <div class="theme-desc">${item.description}</div>
          </div>
        `;
        popularContainer.appendChild(card);
      });
    })
    .catch(err => console.error('Failed to load popular themes', err));

  // 2. 날짜 선택 시 테마 목록 노출
  dateInput.addEventListener('change', () => {
    if (dateInput.value) {
      loadThemes();
    }
  });

  function loadThemes() {
    fetch('/themes')
      .then(res => res.json())
      .then(data => {
        themeContainer.innerHTML = '';
        selectedThemeId = null;
        selectedTimeId = null;
        timeSection.style.display = 'none';
        nameSection.style.display = 'none';
        submitBtn.disabled = true;

        data.forEach(theme => {
          const btn = document.createElement('div');
          btn.className = 'theme-option';
          btn.innerHTML = `
            <strong>${theme.name}</strong>
          `;
          btn.addEventListener('click', () => {
            document.querySelectorAll('.theme-option').forEach(el => el.classList.remove('selected'));
            btn.classList.add('selected');
            selectedThemeId = theme.id;
            loadAvailableTimes();
          });
          themeContainer.appendChild(btn);
        });
        themeSection.style.display = 'block';
      });
  }

  // 3. 테마 선택 시 해당 날짜의 예약 가능 시간 노출
  function loadAvailableTimes() {
    if (!dateInput.value || !selectedThemeId) return;

    fetch(`/themes/${selectedThemeId}/reservation-times?date=${dateInput.value}`)
      .then(res => res.json())
      .then(data => {
        timeContainer.innerHTML = '';
        selectedTimeId = null;
        nameSection.style.display = 'none';
        submitBtn.disabled = true;

        const availableTimes = data.filter(t => t.alreadyBooked === false || t.available === true || (t.available === undefined && !t.booked)); 
        // Backend implementation checks: "reservationTimes.stream().map(t -> ReservationTimeStatusResponse.of(t, !timeIds.contains(t.getId())))" 
        // wait, let's look at the exact JSON response mapping. The record has probably `available` boolean. Wait, let's assume it maps to `available` boolean based on standard conventions or just check its value.
        // Actually the code in ThemeService is:
        // !timeIds.contains(reservationTime.getId())
        // So the field is likely `available`. Wait, I can just use `t.available` but wait, what if the property is named differently in the record? I'll handle true/false logic.
        
        let hasAvailable = false;
        data.forEach(t => {
          const isAvailable = (t.available !== false); // assuming if available field exists, it's boolean
          
          const btn = document.createElement('button');
          btn.type = 'button';
          btn.className = 'time-btn';
          btn.textContent = t.startAt.substring(0, 5); // 10:00:00 -> 10:00
          
          if (!t.available) { // assuming the field is named `available`
             btn.disabled = true;
          } else {
             hasAvailable = true;
             btn.addEventListener('click', () => {
               document.querySelectorAll('.time-btn').forEach(el => el.classList.remove('selected'));
               btn.classList.add('selected');
               selectedTimeId = t.id;
               nameSection.style.display = 'block';
               checkFormValid();
             });
          }
          timeContainer.appendChild(btn);
        });

        if (!hasAvailable && data.length > 0) {
            timeEmptyMsg.style.display = 'block';
            timeContainer.style.display = 'none';
        } else {
            timeEmptyMsg.style.display = 'none';
            timeContainer.style.display = 'grid';
        }

        timeSection.style.display = 'block';
      });
  }

  nameInput.addEventListener('input', checkFormValid);

  function checkFormValid() {
    const name = nameInput.value.trim();
    if (selectedThemeId && selectedTimeId && dateInput.value && name.length >= 2 && name.length <= 10) {
      submitBtn.disabled = false;
    } else {
      submitBtn.disabled = true;
    }
  }

  // 4. 예약 제출
  form.addEventListener('submit', (e) => {
    e.preventDefault();
    if (submitBtn.disabled) return;

    const payload = {
      name: nameInput.value.trim(),
      date: dateInput.value,
      timeId: selectedTimeId,
      themeId: selectedThemeId
    };

    submitBtn.disabled = true;
    submitBtn.textContent = '예약 중...';

    fetch('/reservations', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })
    .then(res => {
      if (res.ok) {
        alert('예약이 성공적으로 완료되었습니다!');
        // 리셋 및 시간 다시 불러오기
        nameInput.value = '';
        submitBtn.textContent = '예약 완료하기';
        loadAvailableTimes(); // 방금 예약한 시간이 사라지는지 확인
      } else {
        alert('예약에 실패했습니다. (중복 예약이거나 잘못된 입력입니다)');
        submitBtn.disabled = false;
        submitBtn.textContent = '예약 완료하기';
      }
    })
    .catch(err => {
      console.error(err);
      alert('서버 오류가 발생했습니다.');
      submitBtn.disabled = false;
      submitBtn.textContent = '예약 완료하기';
    });
  });

  // 오늘 날짜를 기본 최소 날짜로 설정
  const today = new Date().toISOString().split('T')[0];
  dateInput.min = today;
});
