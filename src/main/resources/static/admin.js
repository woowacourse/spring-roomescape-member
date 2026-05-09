// /Users/smini/Desktop/resunmini/우테코 8기/spring-roomescape-member/src/main/resources/static/admin.js

document.addEventListener('DOMContentLoaded', () => {
    loadThemesForAdmin(); // 관리자 페이지용 테마 로드
    loadAdminSchedules(); // 관리자 페이지용 스케줄 로드
    loadReservations(); // 페이지 로드 시 예약 목록도 바로 불러옵니다.

    // 테마 추가 버튼 이벤트
    document.getElementById('add-theme-btn').addEventListener('click', addTheme);

    // 스케줄 추가 버튼 이벤트
    document.getElementById('add-schedule-btn').addEventListener('click', addSchedule);

    // 전체 예약 조회 버튼 이벤트
    document.getElementById('load-reservations-btn').addEventListener('click', loadReservations);
});

// =================================================================================================
// ✅ 테마 관리 기능
// =================================================================================================

// 테마 목록 불러오기 (GET /themes)
// 스케줄 추가 폼의 드롭다운을 채우고, 테마 목록 테이블을 그립니다.
function loadThemesForAdmin() {
    fetch('/themes')
        .then(response => {
            if (!response.ok) throw new Error('테마 목록을 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(themes => {
            const scheduleThemeSelect = document.getElementById('schedule-theme-select');
            scheduleThemeSelect.innerHTML = '<option value="">테마를 선택하세요</option>'; // 초기화

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

                // 스케줄 추가 폼의 테마 드롭다운에도 추가
                const option = document.createElement('option');
                option.value = theme.id;
                option.textContent = theme.name;
                scheduleThemeSelect.appendChild(option);

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
    const requiredTime = document.getElementById('theme-required-time-input').value;

    if (!name || !requiredTime) {
        alert('테마명과 소요시간을 모두 입력해주세요.');
        return;
    }

    // 💡 입력받은 분(예: 120)을 백엔드가 인식할 수 있는 "02:00" 형태(HH:mm)로 변환합니다.
    const totalMinutes = parseInt(requiredTime, 10);
    const hours = String(Math.floor(totalMinutes / 60)).padStart(2, '0');
    const minutes = String(totalMinutes % 60).padStart(2, '0');
    const formattedTime = `${hours}:${minutes}`;

    fetch('/admin/themes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, description: "", imageUrl: "", requiredTime: formattedTime })
    })
        .then(response => {
            if (response.status === 201) {
                alert('테마가 성공적으로 추가되었습니다.');
                document.getElementById('theme-name-input').value = '';
                document.getElementById('theme-required-time-input').value = '';
                loadThemesForAdmin(); // 목록 새로고침
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
                loadThemesForAdmin(); // 목록 새로고침
            } else {
                response.json().then(errorBody => {
                    alert(`테마 삭제 실패: ${errorBody.message || '알 수 없는 오류'}`);
                });
            }
        })
        .catch(error => console.error('테마 삭제 중 에러:', error));
}

// =================================================================================================
// ✅ 스케줄 관리 기능
// =================================================================================================

// 스케줄 추가 (POST /admin/schedules)
function addSchedule() {
    const date = document.getElementById('schedule-date-input').value;
    const time = document.getElementById('schedule-time-input').value;
    const themeId = document.getElementById('schedule-theme-select').value;

    if (!date || !time || !themeId) {
        alert('날짜, 시간, 테마를 모두 선택해주세요.');
        return;
    }

    fetch('/admin/schedules', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ date, time, themeId: parseInt(themeId) })
    })
        .then(response => {
            if (response.status === 201) {
                alert('스케줄이 성공적으로 추가되었습니다.');
                document.getElementById('schedule-date-input').value = '';
                document.getElementById('schedule-time-input').value = '';
                document.getElementById('schedule-theme-select').value = '';
                loadAdminSchedules(); // 목록 새로고침
            } else {
                response.json().then(errorBody => {
                    alert(`스케줄 추가 실패: ${errorBody.message || '알 수 없는 오류'}`);
                });
            }
        })
        .catch(error => console.error('스케줄 추가 중 에러:', error));
}

// 관리자용 스케줄 목록 불러오기 (GET /admin/schedules)
function loadAdminSchedules() {
    fetch('/admin/schedules')
        .then(response => {
            if (!response.ok) throw new Error('스케줄 목록을 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(schedules => {
            const scheduleListBody = document.getElementById('schedule-list-body');
            scheduleListBody.innerHTML = ''; // 기존 목록 초기화

            if (schedules.length === 0) {
                scheduleListBody.innerHTML = '<tr><td colspan="5" class="empty-message">등록된 스케줄이 없습니다.</td></tr>';
                return;
            }

            schedules.forEach(schedule => {
                const row = scheduleListBody.insertRow();
                row.insertCell().textContent = schedule.id;
                row.insertCell().textContent = schedule.startAt.split('T')[0]; // 날짜만 추출
                row.insertCell().textContent = schedule.startAt.split('T')[1].substring(0, 5); // 시간만 추출
                row.insertCell().textContent = schedule.themeName;
                const deleteCell = row.insertCell();
                const deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.className = 'delete-btn';
                deleteButton.addEventListener('click', () => deleteAdminSchedule(schedule.id));
                deleteCell.appendChild(deleteButton);
            });
        })
        .catch(error => console.error('스케줄 목록 로드 중 에러:', error));
}

// 스케줄 삭제 (DELETE /admin/schedules/{id})
function deleteAdminSchedule(id) {
    if (!confirm('정말로 이 스케줄을 삭제하시겠습니까?')) {
        return;
    }
    fetch(`/admin/schedules/${id}`, { method: 'DELETE' })
        .then(response => {
            if (response.status === 204) { alert('스케줄이 성공적으로 삭제되었습니다.'); loadAdminSchedules(); }
            else { response.json().then(errorBody => { alert(`스케줄 삭제 실패: ${errorBody.message || '알 수 없는 오류'}`); }); }
        }).catch(error => console.error('스케줄 삭제 중 에러:', error));
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
