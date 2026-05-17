document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchNameInput = document.getElementById('search-name');
    const myReservationList = document.getElementById('my-reservation-list');

    // Modal elements
    const modal = document.getElementById('modify-modal');
    const modForm = document.getElementById('modify-form');
    const modId = document.getElementById('mod-id');
    const modTheme = document.getElementById('mod-theme');
    const modDate = document.getElementById('mod-date');
    const modTime = document.getElementById('mod-time');
    const modCancelBtn = document.getElementById('mod-cancel-btn');

    // Prevent past dates
    const today = new Date().toISOString().split('T')[0];
    modDate.min = today;

    let allThemes = [];

    // Init themes for modal
    fetch('/themes').then(r => r.json()).then(data => {
        allThemes = data;
        modTheme.innerHTML = '<option value="">테마를 선택하세요</option>';
        data.forEach(t => {
            modTheme.innerHTML += `<option value="${t.id}">${t.name}</option>`;
        });
    });

    searchForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        await loadMyReservations(searchNameInput.value);
    });

    async function loadMyReservations(name) {
        const res = await fetch(`/reservations?name=${encodeURIComponent(name)}`);
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            alert(err.message || '조회 중 오류가 발생했습니다.');
            return;
        }
        const data = await res.json();
        renderReservations(data);
    }

    function renderReservations(data) {
        if (data.length === 0) {
            myReservationList.innerHTML = '<tr><td colspan="5" style="text-align:center; padding: 2rem;">예약 내역이 없습니다.</td></tr>';
            return;
        }

        myReservationList.innerHTML = '';
        data.forEach(r => {
            myReservationList.innerHTML += `
        <tr>
          <td>${r.id}</td>
          <td>${r.date}</td>
          <td>${r.time ? r.time.startAt : ''}</td>
          <td>${r.theme ? r.theme.name : ''}</td>
          <td style="text-align:center;">
            <button class="btn-primary" style="padding: 0.5rem; font-size: 0.9rem; width: auto;" onclick="openModifyModal(${r.id}, '${r.date}', ${r.theme?.id}, ${r.time?.id})">수정</button>
            <button class="btn-danger" style="padding: 0.5rem; font-size: 0.9rem;" onclick="cancelReservation(${r.id})">취소</button>
          </td>
        </tr>
      `;
        });
    }

    window.cancelReservation = async (id) => {
        if (!confirm('정말로 이 예약을 취소하시겠습니까?')) return;
        const res = await fetch(`/reservations/${id}`, {method: 'DELETE'});
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            alert(err.message || '오류가 발생했습니다.');
        } else {
            alert('예약이 취소되었습니다.');
            loadMyReservations(searchNameInput.value);
        }
    };

    window.openModifyModal = (id, date, themeId, timeId) => {
        modId.value = id;
        modDate.value = date;
        modTheme.value = themeId;
        checkAvailableTimes(timeId); // Load times and select the current one
        modal.style.display = 'flex';
    };

    modCancelBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    function checkAvailableTimes(preselectTimeId = null) {
        const themeId = modTheme.value;
        const date = modDate.value;
        if (!themeId || !date) {
            modTime.innerHTML = '<option value="">날짜와 테마를 먼저 선택하세요</option>';
            modTime.disabled = true;
            return;
        }

        fetch(`/themes/${themeId}/reservation-times?date=${date}`)
            .then(res => res.json())
            .then(times => {
                modTime.innerHTML = '<option value="">시간을 선택하세요</option>';
                let hasAvailable = false;
                times.forEach(t => {
                    // If the time is available OR if it's the currently selected time for this reservation, show it
                    if (t.available || t.id == preselectTimeId) {
                        hasAvailable = true;
                        const selected = (t.id == preselectTimeId) ? 'selected' : '';
                        modTime.innerHTML += `<option value="${t.id}" ${selected}>${t.startAt}</option>`;
                    }
                });
                if (!hasAvailable) {
                    modTime.innerHTML = '<option value="">해당 날짜에 예약 가능한 시간이 없습니다.</option>';
                    modTime.disabled = true;
                } else {
                    modTime.disabled = false;
                }
            });
    }

    modTheme.addEventListener('change', () => checkAvailableTimes());
    modDate.addEventListener('change', () => checkAvailableTimes());

    modForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = {
            date: modDate.value,
            themeId: modTheme.value,
            timeId: modTime.value
        };

        const res = await fetch(`/reservations/${modId.value}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            alert(err.message || '오류가 발생했습니다.');
        } else {
            alert('예약이 성공적으로 변경되었습니다!');
            modal.style.display = 'none';
            loadMyReservations(searchNameInput.value);
        }
    });
});
