(() => {
    const rows = document.getElementById('rows');
    const addBtn = document.getElementById('add-btn');
    const nameEl = document.getElementById('th-name');
    const descEl = document.getElementById('th-desc');
    const thumbEl = document.getElementById('th-thumb');

    rows.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => deleteRow(btn.closest('tr')));
    });

    addBtn.addEventListener('click', async () => {
        const body = {
            name: nameEl.value.trim(),
            description: descEl.value.trim(),
            thumbnailUrl: thumbEl.value.trim()
        };
        if (!body.name) return alert('이름을 입력해주세요.');
        const res = await fetch('/admin/themes', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        if (!res.ok) return alert('추가 실패');
        const t = await res.json();
        appendRow(t);
        nameEl.value = '';
        descEl.value = '';
        thumbEl.value = '';
    });

    function appendRow(t) {
        const tr = document.createElement('tr');
        tr.dataset.id = t.id;
        tr.innerHTML = `
            <td>${t.id}</td>
            <td>${t.name}</td>
            <td>${t.description ?? ''}</td>
            <td><img src="${t.thumbnailUrl ?? ''}" alt="" style="width:60px;height:45px;object-fit:cover;"/></td>
            <td><button type="button" class="btn btn-danger btn-sm delete-btn">삭제</button></td>`;
        tr.querySelector('.delete-btn').addEventListener('click', () => deleteRow(tr));
        rows.appendChild(tr);
    }

    async function deleteRow(tr) {
        if (!confirm('삭제하시겠습니까?')) return;
        const id = tr.dataset.id;
        const res = await fetch(`/admin/themes/${id}`, {method: 'DELETE'});
        if (res.ok) tr.remove();
        else alert('삭제 실패');
    }
})();
