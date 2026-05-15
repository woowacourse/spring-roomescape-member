(() => {
    const nameInput = document.getElementById('search-name');
    const searchBtn = document.getElementById('search-btn');
    const rows = document.getElementById('reservation-rows');
    const emptyMessageColspan = 7;

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
        if (!name) return alert('이름을 입력해주세요.');
        try {
            const list = await RoomescapeApi.request(
                `/reservations?name=${encodeURIComponent(name)}`,
                undefined,
                '예약 조회에 실패했습니다.'
            );
            renderRows(list);
        } catch (e) {
            if (e.code === 'RESERVATION_NOT_FOUND') {
                showEmpty('예약이 없습니다.');
                return;
            }

            alert(e.message);
        }
    }

    function renderRows(list) {
        if (list.length === 0) {
            showEmpty('예약이 없습니다.');
            return;
        }
        rows.innerHTML = '';
        list.forEach(r => rows.appendChild(createReadRow(r)));
    }

    function showEmpty(message) {
        rows.innerHTML = `<tr><td colspan="${emptyMessageColspan}" style="text-align:center; color:#888;">${message}</td></tr>`;
    }

    function createReadRow(reservation) {
        const tr = document.createElement('tr');
        tr.dataset.id = reservation.id;
        tr.innerHTML = `
            <td>${reservation.id}</td>
            <td>${escapeHtml(reservation.name)}</td>
            <td>${escapeHtml(reservation.theme.name)}</td>
            <td>${reservation.date}</td>
            <td>${reservation.time.startAt}</td>
            <td><button type="button" class="btn btn-outline btn-sm edit-btn">변경</button></td>
            <td><button type="button" class="btn btn-danger btn-sm cancel-btn">취소</button></td>`;
        tr.querySelector('.edit-btn').addEventListener('click', () => renderEditRow(tr, reservation));
        tr.querySelector('.cancel-btn').addEventListener('click', () => cancel(reservation.id, tr));

        return tr;
    }

    function renderEditRow(tr, reservation) {
        tr.classList.add('editing-row');
        tr.innerHTML = `
            <td>${reservation.id}</td>
            <td>${escapeHtml(reservation.name)}</td>
            <td>${escapeHtml(reservation.theme.name)}</td>
            <td><input type="date" class="edit-date" value="${reservation.date}"/></td>
            <td><select class="edit-time"><option value="">시간 불러오는 중</option></select></td>
            <td><button type="button" class="btn btn-sm save-btn">저장</button></td>
            <td><button type="button" class="btn btn-outline btn-sm close-btn">닫기</button></td>`;

        const dateInput = tr.querySelector('.edit-date');
        const timeSelect = tr.querySelector('.edit-time');
        const today = new Date();
        today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
        dateInput.min = today.toISOString().slice(0, 10);

        dateInput.addEventListener('change', () => loadTimes(reservation, dateInput, timeSelect));
        tr.querySelector('.save-btn').addEventListener('click', () => update(reservation, tr, dateInput, timeSelect));
        tr.querySelector('.close-btn').addEventListener('click', () => tr.replaceWith(createReadRow(reservation)));

        loadTimes(reservation, dateInput, timeSelect);
    }

    async function loadTimes(reservation, dateInput, timeSelect) {
        const date = dateInput.value;
        if (!date) {
            timeSelect.innerHTML = '<option value="">날짜 선택</option>';
            return;
        }

        try {
            const times = await RoomescapeApi.request(
                `/themes/${reservation.theme.id}/times?date=${date}`,
                undefined,
                '예약 가능한 시간을 불러오지 못했습니다.'
            );
            renderTimeOptions(timeSelect, times, reservation, date);
        } catch (e) {
            timeSelect.innerHTML = '<option value="">조회 실패</option>';
            alert(e.message);
        }
    }

    function renderTimeOptions(timeSelect, times, reservation, date) {
        const options = [...times];
        const isCurrentDate = date === reservation.date;
        const hasCurrentTime = options.some(t => t.id === reservation.time.id);

        if (isCurrentDate && !hasCurrentTime) {
            options.unshift(reservation.time);
        }

        if (options.length === 0) {
            timeSelect.innerHTML = '<option value="">예약 가능한 시간이 없습니다</option>';
            return;
        }

        timeSelect.innerHTML = '<option value="">선택</option>';
        options.forEach(time => {
            const option = document.createElement('option');
            option.value = time.id;
            option.textContent = time.startAt;
            option.selected = isCurrentDate && time.id === reservation.time.id;
            timeSelect.appendChild(option);
        });
    }

    async function update(reservation, tr, dateInput, timeSelect) {
        const body = {
            date: dateInput.value,
            timeId: Number(timeSelect.value)
        };
        if (!body.date) return alert('날짜를 선택해주세요.');
        if (!body.timeId) return alert('시간을 선택해주세요.');

        try {
            const updated = await RoomescapeApi.request(`/reservations/${reservation.id}`, {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(body)
            }, '예약 변경에 실패했습니다.');
            tr.replaceWith(createReadRow(updated));
            alert('예약이 변경되었습니다.');
        } catch (e) {
            alert(e.message);
        }
    }

    async function cancel(id, tr) {
        if (!confirm('예약을 취소하시겠습니까?')) return;
        try {
            await RoomescapeApi.request(`/reservations/${id}`, {method: 'DELETE'}, '예약 취소에 실패했습니다.');
            tr.remove();
            if (rows.children.length === 0) {
                showEmpty('예약이 없습니다.');
            }
        } catch (e) {
            alert(e.message);
        }
    }

    function escapeHtml(value) {
        return String(value)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#039;');
    }
})();
