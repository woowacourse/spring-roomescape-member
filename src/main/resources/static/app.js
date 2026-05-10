// /Users/smini/Desktop/resunmini/우테코 8기/spring-roomescape-member/src/main/resources/static/app.js

// 현재 로그인한 사용자 이름을 저장할 변수
let currentUser = '';

// 로그인 버튼 클릭 이벤트
document.getElementById('login-btn').addEventListener('click', () => {
    const nameInput = document.getElementById('login-name').value.trim();

    if (!nameInput) {
        alert('이름을 입력해주세요.');
        return;
    }

    // 일반 사용자일 경우
    currentUser = nameInput;
    document.getElementById('login-section').classList.add('hidden');
    document.getElementById('reservation-section').classList.remove('hidden');

    // 💡 관리자일 경우, 관리자 페이지로 가는 버튼을 보여줍니다.
    if (currentUser === '루크') {
        document.getElementById('admin-page-btn').classList.remove('hidden');
    }

    loadThemes(); // 일반 유저 화면이 뜨면 테마를 불러옵니다.
    loadPopularThemes(); // 인기 테마 통계도 함께 불러옵니다.
});

// 페이지 로드 시 날짜 입력 필드의 최소 날짜를 오늘로 설정
document.addEventListener('DOMContentLoaded', () => {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date-input').min = today;
});

// 이름 입력창에서 엔터키를 눌렀을 때 '시작하기' 버튼 클릭 효과 주기
document.getElementById('login-name').addEventListener('keyup', (event) => {
    if (event.key === 'Enter') {
        document.getElementById('login-btn').click();
    }
});

// 관리자 페이지 버튼 클릭 이벤트
document.getElementById('admin-page-btn').addEventListener('click', () => {
    window.location.href = '/admin.html';
});

// =================================================================================================
// ✅ TODO 1: 테마 목록을 API(GET /themes)로 가져와서 <select>에 채우기
// API 명세: GET /themes -> [ { id, name, ... }, ... ]
// =================================================================================================
function loadThemes() {
    fetch('/themes')
        .then(response => {
            if (!response.ok) throw new Error('서버에서 테마 목록을 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(themes => {
            const themeContainer = document.getElementById('theme-list-container');
            themeContainer.innerHTML = ''; // 기존 내용 초기화

            // 받아온 themes 배열을 순회하면서 테마 카드를 만듭니다.
            themes.forEach(theme => {
                const card = document.createElement('div');
                card.className = 'theme-card';
                card.dataset.themeId = theme.id; // 데이터 속성에 ID 저장
                card.innerHTML = `
                    <img src="${theme.imageUrl || 'https://i.imgur.com/5nL2kE7.png'}" alt="${theme.name}">
                    <h4>${theme.name}</h4>
                    <p class="theme-description">${theme.description}</p>
                    <p class="theme-time">🕒 ${theme.requiredTime}</p>
                `;

                // 카드 클릭 시 선택 효과를 주는 이벤트 리스너 추가
                card.addEventListener('click', () => {
                    // 모든 카드에서 'selected' 클래스 제거
                    document.querySelectorAll('.theme-card').forEach(c => c.classList.remove('selected'));
                    // 클릭된 카드에만 'selected' 클래스 추가
                    card.classList.add('selected');
                });

                themeContainer.appendChild(card);
            });
        })
        .catch(error => console.error(error));
}

// =================================================================================================
// ✅ 인기 테마 통계 조회
// API 명세: GET /themes?sort=reservations&limit=10&days=7
// =================================================================================================
function loadPopularThemes() {
    fetch('/themes?sort=reservations&limit=10&days=7')
        .then(response => {
            if (!response.ok) throw new Error('인기 테마 통계를 불러오는데 실패했습니다.');
            return response.json();
        })
        .then(popularThemes => {
            const popularList = document.getElementById('popular-themes-list');
            popularList.innerHTML = ''; // 초기화

            if (popularThemes.length === 0) {
                popularList.innerHTML = '<li style="list-style:none; margin-left:-20px;" class="empty-message">최근 예약된 테마가 없습니다.</li>';
                return;
            }

            popularThemes.forEach(theme => {
                const li = document.createElement('li');
                li.innerHTML = `<strong>${theme.themeName}</strong> <span style="color: #e74c3c; font-size: 0.9em;">(예약 ${theme.reservationCount}건)</span>`;
                popularList.appendChild(li);
            });
        })
        .catch(error => console.error(error));
}

// =================================================================================================
// ✅ TODO 2: '스케줄 조회' 버튼 클릭 시 API(GET /schedules) 호출하여 결과 그리기
// API 명세: GET /schedules?date={date}&themeId={themeId} -> [ { id, startAt, ... }, ... ]
// =================================================================================================
document.getElementById('search-schedule-btn').addEventListener('click', () => {
    // 선택된 테마 카드의 ID를 가져옵니다.
    const selectedThemeCard = document.querySelector('.theme-card.selected');
    const themeId = selectedThemeCard ? selectedThemeCard.dataset.themeId : null;

    const date = document.getElementById('date-input').value;

    if (!themeId || !date) {
        alert("테마와 날짜를 모두 선택해주세요.");
        return;
    }

    // 백틱(`)을 사용해 쿼리 파라미터가 포함된 URL을 만듭니다.
    const url = `/schedules?date=${date}&themeId=${themeId}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('스케줄을 조회하는데 실패했습니다.');
            return response.json();
        })
        .then(schedules => {
            const scheduleList = document.getElementById('schedule-list');
            scheduleList.innerHTML = ''; // 이전 검색 결과가 있다면 초기화

            // 🚨 백엔드에서 이미 '예약 가능한' 스케줄만 필터링해서 줬으므로, 그대로 그립니다!
            if (schedules.length === 0) {
                scheduleList.innerHTML = '<li class="empty-message">예약 가능한 시간이 없습니다.</li>';
                return;
            }

            schedules.forEach(schedule => {
                const li = document.createElement('li');
                // 🚨 공백(' ') 대신 'T'를 기준으로 쪼개서 시간을 추출합니다.
                const time = schedule.startAt.split('T')[1].substring(0, 5); // "HH:mm" 형식으로 추출
                // 🚨 이제 value에 schedule.id를 넣습니다!
                li.innerHTML = `<label><input type="radio" name="schedule" value="${schedule.id}"> <strong>${time}</strong> - 예약 가능 🟢</label>`;
                scheduleList.appendChild(li);
            });
        })
        .catch(error => console.error(error));
});


// =================================================================================================
// ✅ TODO 3: '예약하기' 버튼 클릭 시 API(POST /reservations) 호출
// =================================================================================================
document.getElementById('reserve-btn').addEventListener('click', () => {
    const selectedScheduleRadio = document.querySelector('input[name="schedule"]:checked');

    if (!selectedScheduleRadio) {
        alert("예약할 시간을 선택해주세요.");
        return;
    }

    // 🚨 이제 선택된 라디오 버튼의 value는 scheduleId입니다!
    const scheduleId = selectedScheduleRadio.value;

    // 서버로 보낼 데이터 객체 (백엔드 DTO와 모양을 맞춰야 합니다)
    const reservationData = {
        scheduleId: parseInt(scheduleId), // 🚨 scheduleId를 보냅니다.
        name: currentUser // 처음에 로그인할 때 저장해둔 이름을 사용합니다.
    };

    fetch('/reservations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(reservationData),
    })
        .then(response => {
            if (response.status === 201) { // 201 Created
                alert("예약이 완료되었습니다!");
                // 페이지를 새로고침하는 대신, 변경된 부분만 다시 불러옵니다.
                loadPopularThemes(); // 1. 인기 테마 목록 새로고침
                document.getElementById('search-schedule-btn').click(); // 2. 현재 날짜/테마의 스케줄 목록 새로고침
            } else {
                // 서버 응답이 JSON이 아닐 수도 있으므로 텍스트로 먼저 받아서 처리합니다.
                response.text().then(text => {
                    try {
                        const errorBody = JSON.parse(text);
                        alert(`예약에 실패했습니다: ${errorBody.message || '알 수 없는 오류'}`);
                    } catch (e) {
                        alert(`예약에 실패했습니다. (상태 코드: ${response.status})`);
                        console.error("서버 에러 응답 내용:", text);
                    }
                });
            }
        })
        .catch(error => console.error('예약 요청 중 에러 발생:', error));
});
