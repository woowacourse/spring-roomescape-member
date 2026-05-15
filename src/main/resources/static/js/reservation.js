(() => {
    const themeBox = document.querySelector('.selected-theme');
    if (!themeBox) return;
    const themeId = themeBox.dataset.themeId;
    const dateInput = document.getElementById('date-input');
    const timeList = document.getElementById('time-list');
    const nameInput = document.getElementById('name-input');
    const submitBtn = document.getElementById('submit-btn');

    let selectedTimeId = null;
    const today = new Date();
    today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
    dateInput.min = today.toISOString().slice(0, 10);

    dateInput.addEventListener('change', loadTimes);

    async function loadTimes() {
        selectedTimeId = null;
        const date = dateInput.value;
        if (!date) return;
        try {
            const res = await apiFetch(`/themes/${themeId}/times?date=${date}`);
            const times = await res.json();
            renderTimes(times, date);
        } catch (e) {
            timeList.innerHTML = '';
            toastError(e);
        }
    }

    function renderTimes(times, date) {
        if (times.length === 0) {
            timeList.innerHTML = '<span style="color:#888;">예약 가능한 시간이 없습니다.</span>';
            return;
        }
        timeList.innerHTML = '';
        times.forEach(t => {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'time-button';
            btn.textContent = t.startAt;
            btn.dataset.timeId = t.id;
            if (isPastSlot(date, t.startAt)) {
                btn.disabled = true;
                btn.title = '이미 지난 시간입니다.';
            } else {
                btn.addEventListener('click', () => {
                    document.querySelectorAll('.time-button').forEach(b => b.classList.remove('selected'));
                    btn.classList.add('selected');
                    selectedTimeId = t.id;
                });
            }
            timeList.appendChild(btn);
        });
    }

    submitBtn.addEventListener('click', async () => {
        const name = nameInput.value.trim();
        const date = dateInput.value;
        if (!name) return showToast('이름을 입력해주세요.', null, 'error');
        if (!date) return showToast('날짜를 선택해주세요.', null, 'error');
        if (!selectedTimeId) return showToast('시간을 선택해주세요.', null, 'error');

        try {
            const res = await apiFetch('/reservations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name,
                    date,
                    timeId: selectedTimeId,
                    themeId: Number(themeId)
                })
            });
            const reservation = await res.json();
            sessionStorage.setItem('lastReservation', JSON.stringify(reservation));
            location.href = '/reservation/success';
        } catch (e) {
            toastError(e);
        }
    });
})();
