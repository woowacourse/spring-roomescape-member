(() => {
    const nameInput = document.getElementById('search-name');
    const searchBtn = document.getElementById('search-btn');
    const rows = document.getElementById('reservation-rows');

    const modal = document.getElementById('edit-modal');
    const modalCurrent = document.getElementById('edit-current');
    const editDate = document.getElementById('edit-date');
    const editTimeList = document.getElementById('edit-time-list');
    const editCloseBtn = document.getElementById('edit-close');
    const editSaveBtn = document.getElementById('edit-save');

    // 변경 중인 예약: {id, themeId, row}
    let editing = null;
    let selectedTimeId = null;

    const today = new Date();
    today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
    editDate.min = today.toISOString().slice(0, 10);

    const params = new URLSearchParams(location.search);
    const initial = params.get('name');
    if (initial) {
        nameInput.value = initial;
        search();
    }

    searchBtn.addEventListener('click', search);
    nameInput.addEventListener('keydown', e => {
        if (e.key === 'Enter') search();
    });

    async function search() {
        const name = nameInput.value.trim();
        if (!name) return;
        try {
            const res = await apiFetch(`/reservations?name=${encodeURIComponent(name)}`);
            const list = await res.json();
            renderRows(list);
        } catch (e) {
            if (e.status === 404) {
                renderEmpty();
                return;
            }
            toastError(e);
        }
    }

    function renderEmpty() {
        rows.innerHTML = '<tr><td colspan="6" style="text-align:center; color:#888;">예약이 없습니다.</td></tr>';
    }

    function renderRows(list) {
        if (list.length === 0) {
            renderEmpty();
            return;
        }
        rows.innerHTML = '';
        list.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${r.id}</td>
                <td>${r.name}</td>
                <td class="cell-theme">${r.theme.name}</td>
                <td class="cell-date">${r.date}</td>
                <td class="cell-time">${r.time.startAt}</td>
                <td>
                    <div class="cell-actions">
                        <button type="button" class="btn btn-outline btn-sm edit-btn">변경</button>
                        <button type="button" class="btn btn-danger btn-sm cancel-btn">취소</button>
                    </div>
                </td>`;
            tr.querySelector('.edit-btn').addEventListener('click', () => openModal(r, tr));
            tr.querySelector('.cancel-btn').addEventListener('click', () => cancel(r.id, tr));
            rows.appendChild(tr);
        });
    }

    async function cancel(id, tr) {
        if (!confirm('예약을 취소하시겠습니까?')) return;
        try {
            await apiFetch(`/reservations/${id}`, {method: 'DELETE'});
            tr.remove();
            toastSuccess('예약이 취소되었습니다.');
        } catch (e) {
            toastError(e);
        }
    }

    function openModal(r, tr) {
        editing = {id: r.id, themeId: r.theme.id, row: tr};
        selectedTimeId = null;
        modalCurrent.textContent = `현재 예약: ${r.theme.name} / ${r.date} ${r.time.startAt}`;
        editDate.value = r.date;
        modal.hidden = false;
        loadTimes();
    }

    function closeModal() {
        modal.hidden = true;
        editing = null;
        selectedTimeId = null;
        editTimeList.innerHTML = '<span style="color:#888; font-size:13px;">날짜를 먼저 선택하세요.</span>';
    }

    editCloseBtn.addEventListener('click', closeModal);
    modal.addEventListener('click', e => {
        if (e.target === modal) closeModal();
    });
    editDate.addEventListener('change', loadTimes);

    async function loadTimes() {
        selectedTimeId = null;
        const date = editDate.value;
        if (!date || !editing) return;
        try {
            const res = await apiFetch(`/themes/${editing.themeId}/times?date=${date}`);
            const times = await res.json();
            renderTimes(times, date);
        } catch (e) {
            editTimeList.innerHTML = '';
            toastError(e);
        }
    }

    function renderTimes(times, date) {
        if (times.length === 0) {
            editTimeList.innerHTML = '<span style="color:#888; font-size:13px;">변경 가능한 시간이 없습니다.</span>';
            return;
        }
        editTimeList.innerHTML = '';
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
                    editTimeList.querySelectorAll('.time-button').forEach(b => b.classList.remove('selected'));
                    btn.classList.add('selected');
                    selectedTimeId = t.id;
                });
            }
            editTimeList.appendChild(btn);
        });
    }

    editSaveBtn.addEventListener('click', async () => {
        if (!editing) return;
        const date = editDate.value;
        if (!date) return showToast('날짜를 선택해주세요.', 'error');
        if (!selectedTimeId) return showToast('변경할 시간을 선택해주세요.', 'error');

        try {
            const res = await apiFetch(`/reservations/${editing.id}`, {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({date, timeId: selectedTimeId})
            });
            const updated = await res.json();
            const row = editing.row;
            row.querySelector('.cell-date').textContent = updated.date;
            row.querySelector('.cell-time').textContent = updated.time.startAt;
            closeModal();
            toastSuccess('예약이 변경되었습니다.');
        } catch (e) {
            toastError(e);
        }
    });
})();
