(() => {
    const rows = document.getElementById('rows');
    const addBtn = document.getElementById('add-btn');
    const startAtEl = document.getElementById('t-start-at');

    rows.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => deleteRow(btn.closest('tr')));
    });

    addBtn.addEventListener('click', async () => {
        const startAt = startAtEl.value;
        if (!startAt) return alert('시간을 입력해주세요.');
        const res = await fetch('/times', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({startAt})
        });
        if (!res.ok) return alert('추가 실패');
        const t = await res.json();
        appendRow(t);
        startAtEl.value = '';
    });

    function appendRow(t) {
        const tr = document.createElement('tr');
        tr.dataset.id = t.id;
        tr.innerHTML = `
            <td>${t.id}</td>
            <td>${t.startAt}</td>
            <td><button type="button" class="btn btn-danger btn-sm delete-btn">삭제</button></td>`;
        tr.querySelector('.delete-btn').addEventListener('click', () => deleteRow(tr));
        rows.appendChild(tr);
    }

    async function deleteRow(tr) {
        if (!confirm('삭제하시겠습니까?')) return;
        const id = tr.dataset.id;
        const res = await fetch(`/times/${id}`, {method: 'DELETE'});
        if (res.ok) tr.remove();
        else alert('삭제 실패');
    }
})();
