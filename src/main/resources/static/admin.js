// /Users/smini/Desktop/resunmini/우테코 8기/spring-roomescape-member/src/main/resources/static/admin.js

document.addEventListener('DOMContentLoaded', () => {
    loadThemes();
    loadReservations(); // 페이지 로드 시 예약 목록도 바로 불러옵니다.

    // 테마 추가 버튼 이벤트
    document.getElementById('add-theme-btn').addEventListener('click', addTheme);

    // 전체 예약 조회 버튼 이벤트
    document.getElementById('load-reservations-btn').addEventListener('click', loadReservations);
});

// =================================================================================================
// ✅ 테마 관리 기능
// =================================================================================================

// 테마 목록 불러오기 (GET /themes)
// 관리자 페이지에서도 사용자용 테마 목록 API를 재활용합니다.
function loadThemes() {
    fetch('/themes')
        .then(response => {
            if (!response.ok) throw new Error('테마 목록을 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(themes => {
            const themeListBody = document.getElementById('theme-list-body');
            themeListBody.innerHTML = ''; // 기존 목록 초기화

            if (themes.length === 0) {
                themeListBody.innerHTML = '<tr><td colspan="4" class="empty-message">등록된 테마가 없습니다.</td></tr>';
                return;
            }

            themes.forEach(theme => {
                const row = themeListBody.insertRow();
                row.insertCell().textContent = theme.id;
                row.insertCell().textContent = theme.name;
                row.insertCell().textContent = theme.requiredTime;
                const deleteCell = row.insertCell();
                const deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.className = 'delete-btn';
                deleteButton.addEventListener('click', () => deleteTheme(theme.id));
                deleteCell.appendChild(deleteButton);
            });
        })
        .catch(error => console.error('테마 목록 로드 중 에러:', error));
}

// 테마 추가 (POST /admin/themes)
function addTheme() {
    const name = document.getElementById('theme-name-input').value.trim();
    const requiredTimeInput = document.getElementById('theme-required-time-input').value;

    if (!name || !requiredTimeInput) {
        alert('테마명과 소요시간을 모두 입력해주세요.');
        return;
    }

    // 💡 입력받은 분(예: 120)을 백엔드가 인식할 수 있는 "02:00" 형태(HH:mm)로 변환합니다.
    const totalMinutes = parseInt(requiredTimeInput, 10);
    const hours = String(Math.floor(totalMinutes / 60)).padStart(2, '0');
    const minutes = String(totalMinutes % 60).padStart(2, '0');
    const formattedTime = `${hours}:${minutes}`;

    fetch('/admin/themes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, description: "", imageUrl: "", requiredTime: formattedTime })
    })
        .then(response => {
            if (response.status === 201) {
                alert('테마가 성공적으로 추가되었습니다.');
                document.getElementById('theme-name-input').value = '';
                document.getElementById('theme-required-time-input').value = '';
                loadThemes(); // 목록 새로고침
            } else {
                response.json().then(errorBody => {
                    alert(`테마 추가 실패: ${errorBody.message || '알 수 없는 오류'}`);
                });
            }
        })
        .catch(error => console.error('테마 추가 중 에러:', error));
}

// 테마 삭제 (DELETE /admin/themes/{id})
function deleteTheme(id) {
    if (!confirm('정말로 이 테마를 삭제하시겠습니까?')) {
        return;
    }

    fetch(`/admin/themes/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.status === 204) {
                alert('테마가 성공적으로 삭제되었습니다.');
                loadThemes(); // 목록 새로고침
            } else {
                response.json().then(errorBody => {
                    alert(`테마 삭제 실패: ${errorBody.message || '알 수 없는 오류'}`);
                });
            }
        })
        .catch(error => console.error('테마 삭제 중 에러:', error));
}

// =================================================================================================
// ✅ 예약 관리 기능
// =================================================================================================

// 전체 예약 목록 불러오기 (GET /admin/reservations)
function loadReservations() {
    fetch('/admin/reservations')
        .then(response => {
            if (!response.ok) throw new Error('예약 목록을 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(reservations => {
            const reservationListBody = document.getElementById('reservation-list-body');
            reservationListBody.innerHTML = ''; // 기존 목록 초기화

            if (reservations.length === 0) {
                reservationListBody.innerHTML = '<tr><td colspan="6" class="empty-message">등록된 예약이 없습니다.</td></tr>';
                return;
            }

            reservations.forEach(reservation => {
                const row = reservationListBody.insertRow();
                row.insertCell().textContent = reservation.reservationId;
                row.insertCell().textContent = reservation.userName;
                row.insertCell().textContent = reservation.themeName;
                row.insertCell().textContent = reservation.startAt;
                row.insertCell().textContent = reservation.endAt;
                const deleteCell = row.insertCell();
                const deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.className = 'delete-btn';
                deleteButton.addEventListener('click', () => deleteReservation(reservation.reservationId));
                deleteCell.appendChild(deleteButton);
            });
        })
        .catch(error => console.error('예약 목록 로드 중 에러:', error));
}

// 예약 삭제 (DELETE /admin/reservations/{id})
function deleteReservation(id) {
    if (!confirm('정말로 이 예약을 삭제하시겠습니까?')) {
        return;
    }

    fetch(`/admin/reservations/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.status === 204) {
                alert('예약이 성공적으로 삭제되었습니다.');
                loadReservations(); // 목록 새로고침
            } else {
                response.json().then(errorBody => {
                    alert(`예약 삭제 실패: ${errorBody.message || '알 수 없는 오류'}`);
                });
            }
        })
        .catch(error => console.error('예약 삭제 중 에러:', error));
}