(() => {
    const themeBox = document.querySelector('.selected-theme');
    if (!themeBox) return;
    const themeId = themeBox.dataset.themeId;
    const dateInput = document.getElementById('date-input');
    const timeList = document.getElementById('time-list');
    const nameInput = document.getElementById('name-input');
    const submitBtn = document.getElementById('submit-btn');

    let selectedTimeId = null;

    dateInput.addEventListener('change', loadTimes);

    async function loadTimes() {
        selectedTimeId = null;
        const date = dateInput.value;
        if (!date) return;
        try {
            const res = await fetch(`/themes/${themeId}/times?date=${date}`);
            if (!res.ok) throw new Error('시간 조회 실패');
            const times = await res.json();
            renderTimes(times);
        } catch (e) {
            timeList.innerHTML = `<span style="color:#a00;">${e.message}</span>`;
        }
    }

    function renderTimes(times) {
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
            btn.addEventListener('click', () => {
                document.querySelectorAll('.time-button').forEach(b => b.classList.remove('selected'));
                btn.classList.add('selected');
                selectedTimeId = t.id;
            });
            timeList.appendChild(btn);
        });
    }

    submitBtn.addEventListener('click', async () => {
        const name = nameInput.value.trim();
        const date = dateInput.value;
        if (!name) return alert('이름을 입력해주세요.');
        if (!date) return alert('날짜를 선택해주세요.');
        if (!selectedTimeId) return alert('시간을 선택해주세요.');

        try {
            const res = await fetch('/reservations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name,
                    date,
                    timeId: selectedTimeId,
                    themeId: Number(themeId)
                })
            });
            if (!res.ok) throw new Error('예약에 실패했습니다.');
            const reservation = await res.json();
            sessionStorage.setItem('lastReservation', JSON.stringify(reservation));
            location.href = '/reservation/success';
        } catch (e) {
            alert(e.message);
        }
    });
})();
