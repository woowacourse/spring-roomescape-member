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

    // 이름이 '루크'인 경우 어드민 페이지로 리다이렉트
    if (nameInput === '루크') {
        alert('관리자님 환영합니다. 관리자 페이지로 이동합니다.');
        window.location.href = '/admin.html'; // 어드민용 HTML을 따로 만드셔야 합니다!
        return;
    }

    // 일반 사용자일 경우
    currentUser = nameInput;
    document.getElementById('login-section').classList.add('hidden');
    document.getElementById('reservation-section').classList.remove('hidden');

    loadThemes(); // 일반 유저 화면이 뜨면 테마를 불러옵니다.
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
            const themeSelect = document.getElementById('theme-select');

            // 받아온 themes 배열을 순회하면서 <option> 태그를 만들어 themeSelect에 추가합니다.
            themes.forEach(theme => {
                const option = document.createElement('option');
                option.value = theme.id; // option의 값으로는 theme의 id를
                option.textContent = theme.name; // 사용자에게 보여줄 텍스트는 theme의 name을 사용
                themeSelect.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

// =================================================================================================
// ✅ TODO 2: '스케줄 조회' 버튼 클릭 시 API(GET /schedules) 호출하여 결과 그리기
// API 명세: GET /schedules?date={date}&themeId={themeId} -> [ { id, startAt, ... }, ... ]
// =================================================================================================
document.getElementById('search-schedule-btn').addEventListener('click', () => {
    const themeId = document.getElementById('theme-select').value;
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

            // 요구사항에 맞춰 10시부터 20시까지의 시간 배열 생성
            const allHours = [];
            for (let i = 10; i <= 20; i++) {
                const hour = i < 10 ? `0${i}:00` : `${i}:00`;
                allHours.push(hour);
            }

            // 생성한 모든 시간대(allHours)를 화면에 그립니다.
            allHours.forEach(time => {
                // API로 받아온 스케줄(=이미 누군가 예약한 스케줄) 중 해당 시간이 있는지 확인
                // startAt이 "2026-05-10 10:00" 형태이므로 time 문자열 포함 여부로 확인
                const isReserved = schedules.find(s => s.startAt.includes(time));

                const li = document.createElement('li');

                if (isReserved) {
                    // 스케줄이 존재함 = 이미 예약됨 (클릭 불가)
                    li.style.backgroundColor = '#e2e8f0';
                    li.style.color = '#94a3b8';
                    li.innerHTML = `<label style="cursor: not-allowed;"><input type="radio" name="schedule" disabled> <del>${time}</del> - 예약 마감 🔴</label>`;
                } else {
                    // 스케줄이 없음 = 빈 방이라 예약 가능! (클릭 가능)
                    // 🚨 아직 DB에 스케줄이 없으므로 value에 '시간(time)' 자체를 넣습니다.
                    li.innerHTML = `<label><input type="radio" name="schedule" value="${time}"> <strong>${time}</strong> - 예약 가능 🟢</label>`;
                }

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

    const selectedTime = selectedScheduleRadio.value; // 예: "10:00"
    const themeId = document.getElementById('theme-select').value;
    const date = document.getElementById('date-input').value;

    // 서버로 보낼 데이터 객체 (백엔드 DTO와 모양을 맞춰야 합니다)
    const reservationData = {
        // 🚨 예약 전이라 scheduleId가 없으므로, 생성에 필요한 모든 정보를 보냅니다.
        date: date,
        time: selectedTime,
        themeId: parseInt(themeId),
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
                window.location.reload(); // 성공 시 페이지 새로고침
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
