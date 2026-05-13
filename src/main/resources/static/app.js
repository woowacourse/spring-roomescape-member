// C:/projects/woowatech/spring-roomescape-member/src/main/resources/static/app.js

let currentUser = '';

// 임시 유저 매핑
function getMappedUserId() {
    const userMap = {'루크': 1, '소낙눈': 2, '포비': 3};
    return userMap[currentUser] || 1;
}

document.getElementById('login-btn').addEventListener('click', () => {
    const nameInput = document.getElementById('login-name').value.trim();
    if (!nameInput) {
        alert('이름을 입력해주세요.');
        return;
    }
    if (nameInput === '루크') {
        alert('관리자님 환영합니다. 관리자 페이지로 이동합니다.');
        window.location.href = '/admin.html';
        return;
    }

    currentUser = nameInput;
    document.getElementById('login-section').classList.add('hidden');
    document.getElementById('reservation-section').classList.remove('hidden');

    loadThemes();
    loadPopularThemes();
    loadMyReservations(); // 💡 로그인 시 내 예약도 불러옵니다!
});

document.addEventListener('DOMContentLoaded', () => {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date-input').min = today;
});

document.getElementById('login-name').addEventListener('keyup', (event) => {
    if (event.key === 'Enter') document.getElementById('login-btn').click();
});

// 테마 목록 조회
function loadThemes() {
    fetch('/themes')
        .then(response => {
            if (!response.ok) throw new Error('테마 로드 실패');
            return response.json();
        })
        .then(themes => {
            const themeContainer = document.getElementById('theme-list-container');
            themeContainer.innerHTML = '';
            themes.forEach(theme => {
                const card = document.createElement('div');
                card.className = 'theme-card';
                card.dataset.themeId = theme.id;
                card.innerHTML = `
                    <img src="${theme.imageUrl || 'https://i.imgur.com/5nL2kE7.png'}" alt="${theme.name}">
                    <h4>${theme.name}</h4>
                    <p class="theme-description">${theme.description}</p>
                    <p class="theme-time">🕒 ${theme.requiredTime}</p>
                `;
                card.addEventListener('click', () => {
                    document.querySelectorAll('.theme-card').forEach(c => c.classList.remove('selected'));
                    card.classList.add('selected');
                });
                themeContainer.appendChild(card);
            });
        }).catch(e => console.error(e));
}

// 인기 테마 통계 조회
function loadPopularThemes() {
    fetch('/themes?sort=reservations&limit=10&days=7')
        .then(res => res.json())
        .then(popularThemes => {
            const popularList = document.getElementById('popular-themes-list');
            popularList.innerHTML = '';
            if (popularThemes.length === 0) {
                popularList.innerHTML = '<li class="empty-message" style="list-style:none; margin-left:-20px;">최근 예약된 테마가 없습니다.</li>';
                return;
            }
            popularThemes.forEach(theme => {
                const li = document.createElement('li');
                li.innerHTML = `<strong>${theme.themeName}</strong> <span style="color: #e74c3c; font-size: 0.9em;">(예약 ${theme.reservationCount}건)</span>`;
                popularList.appendChild(li);
            });
        }).catch(e => console.error(e));
}

// 스케줄 조회 (백엔드 스펙에 맞춤)
document.getElementById('search-schedule-btn').addEventListener('click', () => {
    const selectedThemeCard = document.querySelector('.theme-card.selected');
    const themeId = selectedThemeCard ? selectedThemeCard.dataset.themeId : null;
    const date = document.getElementById('date-input').value;

    if (!themeId || !date) {
        alert("테마와 날짜를 모두 선택해주세요.");
        return;
    }

    fetch(`/schedules?date=${date}&themeId=${themeId}`)
        .then(res => {
            if (!res.ok) throw new Error('스케줄 조회 실패');
            return res.json();
        })
        .then(schedules => {
            const scheduleList = document.getElementById('schedule-list');
            scheduleList.innerHTML = '';

            // 💡 백엔드가 반환한 "예약되지 않은" 스케줄만 리스트로 그립니다.
            if (schedules.length === 0) {
                scheduleList.innerHTML = '<li class="empty-message">해당 날짜에 예약 가능한 스케줄이 없습니다.</li>';
                return;
            }

            schedules.forEach(s => {
                const li = document.createElement('li');
                const startAtStr = s.startAt.replace(' ', 'T');
                const timeStr = startAtStr.split('T')[1].substring(0, 5); // 10:00:00 -> 10:00
                // value에 scheduleId를, data-start-at에 시간을 저장합니다.
                li.innerHTML = `<label><input type="radio" name="schedule" value="${s.id}" data-start-at="${startAtStr}"> <strong>${timeStr}</strong> - 예약 가능 🟢</label>`;
                scheduleList.appendChild(li);
            });
        }).catch(e => console.error(e));
});

// 예약 생성
document.getElementById('reserve-btn').addEventListener('click', () => {
    const selectedScheduleRadio = document.querySelector('input[name="schedule"]:checked');
    if (!selectedScheduleRadio) {
        alert("예약할 시간을 선택해주세요.");
        return;
    }

    const startAt = selectedScheduleRadio.dataset.startAt;
    const themeId = document.querySelector('.theme-card.selected').dataset.themeId;

    const reservationData = {
        startAt: startAt,
        themeId: parseInt(themeId),
        userId: getMappedUserId() // 예약 생성 명세(userId)
    };

    fetch('/reservations', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reservationData),
    }).then(res => {
        if (res.status === 201) {
            alert("예약이 완료되었습니다!");
            loadPopularThemes();
            document.getElementById('search-schedule-btn').click(); // 스케줄 새로고침
            loadMyReservations(); // 내 예약 새로고침
        } else {
            // 💡 서버가 뱉어낸 의도된 에러(400)를 alert로 친절히 보여줍니다.
            res.json().then(err => alert(`예약 실패: ${err.message}`));
        }
    });
});

// =================================================================================================
// ✅ 신규 기능: 내 예약 관리
// =================================================================================================

// 1. 내 예약 불러오기
function loadMyReservations() {
    fetch('/reservations/my', {
        headers: {'X-User-Id': getMappedUserId()} // 💡 서버와의 약속 헤더
    })
        .then(res => {
            if (!res.ok) throw new Error('조회 실패');
            return res.json();
        })
        .then(reservations => {
            const list = document.getElementById('my-reservation-list');
            list.innerHTML = '';
            if (reservations.length === 0) {
                list.innerHTML = '<li class="empty-message">예약 내역이 없습니다.</li>';
                return;
            }

            reservations.forEach(r => {
                const li = document.createElement('li');
                li.style.display = 'flex';
                li.style.justifyContent = 'space-between';
                li.style.alignItems = 'center';
                li.style.padding = '10px';
                li.style.borderBottom = '1px solid #eee';

                const startAt = r.startAt.replace('T', ' ').substring(0, 16);
                const info = document.createElement('div');
                info.innerHTML = `<strong>[${r.themeName}]</strong> <br> <span style="font-size:0.9em; color:#666;">${startAt}</span>`;

                const btnGroup = document.createElement('div');

                const cancelBtn = document.createElement('button');
                cancelBtn.textContent = '취소';
                cancelBtn.className = 'delete-btn';
                cancelBtn.style.marginRight = '5px';
                cancelBtn.onclick = () => cancelReservation(r.reservationId);

                const changeBtn = document.createElement('button');
                changeBtn.textContent = '위에서 선택한 시간으로 변경';
                changeBtn.className = 'primary-btn';
                changeBtn.style.padding = '5px 10px';
                changeBtn.style.fontSize = '0.8em';
                changeBtn.onclick = () => changeReservation(r.reservationId);

                btnGroup.appendChild(cancelBtn);
                btnGroup.appendChild(changeBtn);
                li.appendChild(info);
                li.appendChild(btnGroup);
                list.appendChild(li);
            });
        });
}

// 2. 예약 취소
function cancelReservation(id) {
    if (!confirm('정말로 이 예약을 취소하시겠습니까?')) return;

    fetch(`/reservations/${id}`, {
        method: 'DELETE',
        headers: {'X-User-Id': getMappedUserId()}
    }).then(res => {
        if (res.status === 204 || res.ok) {
            alert('취소되었습니다.');
            loadMyReservations();
            document.getElementById('search-schedule-btn').click();
        } else {
            res.json().then(err => alert(`취소 실패: ${err.message}`));
        }
    });
}

// 3. 예약 변경
function changeReservation(id) {
    const selectedScheduleRadio = document.querySelector('input[name="schedule"]:checked');
    if (!selectedScheduleRadio) {
        alert("위의 스케줄 목록에서 변경하고 싶은 '새로운 시간'을 먼저 선택해주세요!");
        return;
    }

    const newScheduleId = selectedScheduleRadio.value; // 변경 API는 scheduleId를 보냅니다.

    if (!confirm('선택한 시간으로 예약을 변경하시겠습니까?')) return;

    fetch(`/reservations/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': getMappedUserId()
        },
        body: JSON.stringify({scheduleId: parseInt(newScheduleId)})
    }).then(res => {
        if (res.ok) {
            alert('예약이 성공적으로 변경되었습니다!');
            loadMyReservations();
            document.getElementById('search-schedule-btn').click();
        } else {
            // 💡 시간 제한 초과, 권한 등 백엔드 에러를 표시합니다.
            res.json().then(err => alert(`변경 실패: ${err.message}`));
        }
    });
}

document.getElementById('load-my-reservations-btn').addEventListener('click', loadMyReservations);