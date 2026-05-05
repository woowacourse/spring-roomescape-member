const API = '/reservations';
const TIMES_API = '/times';
let isEditing = false;
let cachedTimes = [];

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('add-button').addEventListener('click', startAdd);
    refresh();
});

async function refresh() {
    try {
        const [reservations, times] = await Promise.all([
            fetchJson(API),
            fetchJson(TIMES_API)
        ]);
        cachedTimes = times;
        render(reservations);
    } catch (error) {
        console.error('예약 조회 실패:', error);
    }
}

function render(reservations) {
    const tbody = document.getElementById('table-body');
    tbody.innerHTML = '';

    if (reservations.length === 0) {
        showEmptyState(tbody, 5, '등록된 예약이 없습니다. "예약 추가" 버튼으로 시작해보세요.');
        return;
    }

    reservations.forEach(reservation => {
        const row = tbody.insertRow();
        row.insertCell().textContent = reservation.id;
        row.insertCell().textContent = reservation.name;
        row.insertCell().textContent = reservation.date;
        row.insertCell().textContent = reservation.time ? reservation.time.startAt : '-';

        const actions = row.insertCell();
        actions.className = 'actions';
        actions.appendChild(createButton('삭제', 'btn-danger', () => deleteRow(reservation.id)));
    });
}

function startAdd() {
    if (isEditing) return;
    if (cachedTimes.length === 0) {
        alert('등록된 시간이 없습니다. "시간 관리" 페이지에서 먼저 등록해주세요.');
        return;
    }
    isEditing = true;

    const tbody = document.getElementById('table-body');
    const emptyRow = tbody.querySelector('.empty-state');
    if (emptyRow) emptyRow.closest('tr').remove();

    const row = tbody.insertRow(0);
    row.insertCell().textContent = '';

    const nameInput = createInput('text', '예: 민욱');
    const dateInput = createInput('date');
    const timeSelect = createSelect();
    cachedTimes.forEach(time => {
        const option = document.createElement('option');
        option.value = time.id;
        option.textContent = time.startAt;
        timeSelect.appendChild(option);
    });

    row.insertCell().appendChild(nameInput);
    row.insertCell().appendChild(dateInput);
    row.insertCell().appendChild(timeSelect);

    const actions = row.insertCell();
    actions.className = 'actions';
    actions.appendChild(createButton('저장', 'btn-primary', async () => {
        await saveRow(nameInput.value, dateInput.value, timeSelect.value);
    }));
    actions.appendChild(createButton('취소', 'btn-ghost', () => {
        row.remove();
        isEditing = false;
        refresh();
    }));
}

async function saveRow(name, date, timeId) {
    if (!name.trim() || !date || !timeId) {
        alert('모든 항목을 입력해주세요.');
        return;
    }
    try {
        await fetchJson(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, date, timeId: Number(timeId) })
        });
        isEditing = false;
        refresh();
    } catch (error) {
        console.error('예약 추가 실패:', error);
        alert('예약 추가에 실패했습니다.');
    }
}

async function deleteRow(id) {
    if (!confirm('정말 삭제하시겠습니까?')) return;
    try {
        await fetchJson(`${API}/${id}`, { method: 'DELETE' });
        refresh();
    } catch (error) {
        console.error('예약 삭제 실패:', error);
        alert('삭제에 실패했습니다.');
    }
}
