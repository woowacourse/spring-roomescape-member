(() => {
    const rows = document.getElementById('rows');
    const addBtn = document.getElementById('add-btn');
    const nameEl = document.getElementById('r-name');
    const dateEl = document.getElementById('r-date');
    const timeEl = document.getElementById('r-time');
    const themeEl = document.getElementById('r-theme');
    const themeNameById = {};
    Array.from(themeEl.options).forEach(o => themeNameById[o.value] = o.textContent);
    const timeStartById = {};
    Array.from(timeEl.options).forEach(o => timeStartById[o.value] = o.textContent);

    rows.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => deleteRow(btn.closest('tr')));
    });

    addBtn.addEventListener('click', async () => {
        const body = {
            name: nameEl.value.trim(),
            date: dateEl.value,
            timeId: Number(timeEl.value),
            themeId: Number(themeEl.value)
        };
        if (!body.name || !body.date || !body.timeId || !body.themeId) {
            return alert('모든 항목을 입력해주세요.');
        }
        try {
            const r = await RoomescapeApi.request('/admin/reservations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(body)
            }, '예약 추가에 실패했습니다.');
            appendRow(r);
            nameEl.value = ''; dateEl.value = ''; timeEl.value = ''; themeEl.value = '';
        } catch (e) {
            alert(e.message);
        }
    });

    function appendRow(r) {
        const tr = document.createElement('tr');
        tr.dataset.id = r.id;
        tr.innerHTML = `
            <td>${r.id}</td>
            <td>${r.name}</td>
            <td>${r.date}</td>
            <td>${r.time.startAt}</td>
            <td>${r.theme.name}</td>
            <td><button type="button" class="btn btn-danger btn-sm delete-btn">삭제</button></td>`;
        tr.querySelector('.delete-btn').addEventListener('click', () => deleteRow(tr));
        rows.appendChild(tr);
    }

    async function deleteRow(tr) {
        if (!confirm('삭제하시겠습니까?')) return;
        const id = tr.dataset.id;
        try {
            await RoomescapeApi.request(`/admin/reservations/${id}`, {method: 'DELETE'}, '예약 삭제에 실패했습니다.');
            tr.remove();
        } catch (e) {
            alert(e.message);
        }
    }
})();
