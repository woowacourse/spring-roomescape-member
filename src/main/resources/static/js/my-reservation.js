const API = '/reservations/me';

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('search-form').addEventListener('submit', handleSubmit);
});

async function handleSubmit(event) {
    event.preventDefault();

    const name = document.getElementById('name-input').value.trim();
    if (!name) {
        alert('이름을 입력해주세요.');
        return;
    }

    try {
        const data = await fetchJson(`${API}?name=${encodeURIComponent(name)}`);
        render(data.reservations);
    } catch (error) {
        console.error('내 예약 조회 실패:', error);
        alert(getErrorMessage(error, '내 예약 조회에 실패했습니다.'));
    }
}

function render(reservations) {
    const tbody = document.getElementById('table-body');
    tbody.innerHTML = '';

    if (reservations.length === 0) {
        showEmptyState(tbody, 4, '예약 내역이 없습니다.');
        return;
    }

    reservations.forEach((reservation, index) => {
        const row = tbody.insertRow();
        row.insertCell().textContent = index + 1;
        row.insertCell().textContent = reservation.theme ? reservation.theme.name : '-';
        row.insertCell().textContent = reservation.date;
        row.insertCell().textContent = reservation.time ? reservation.time.startAt : '-';
    });
}
