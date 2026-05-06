(() => {
    const nameInput = document.getElementById('search-name');
    const searchBtn = document.getElementById('search-btn');
    const rows = document.getElementById('reservation-rows');

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
            const res = await fetch(`/reservations?name=${encodeURIComponent(name)}`);
            if (res.status === 404) {
                rows.innerHTML = '<tr><td colspan="6" style="text-align:center; color:#888;">예약이 없습니다.</td></tr>';
                return;
            }
            if (!res.ok) throw new Error('조회 실패');
            const list = await res.json();
            renderRows(list);
        } catch (e) {
            alert(e.message);
        }
    }

    function renderRows(list) {
        if (list.length === 0) {
            rows.innerHTML = '<tr><td colspan="6" style="text-align:center; color:#888;">예약이 없습니다.</td></tr>';
            return;
        }
        rows.innerHTML = '';
        list.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${r.id}</td>
                <td>${r.name}</td>
                <td>${r.theme.name}</td>
                <td>${r.date}</td>
                <td>${r.time.startAt}</td>
                <td><button type="button" class="btn btn-danger btn-sm">취소</button></td>`;
            tr.querySelector('button').addEventListener('click', () => cancel(r.id, tr));
            rows.appendChild(tr);
        });
    }

    async function cancel(id, tr) {
        if (!confirm('예약을 취소하시겠습니까?')) return;
        const res = await fetch(`/reservations/${id}`, {method: 'DELETE'});
        if (res.ok) tr.remove();
        else alert('취소 실패');
    }
})();
