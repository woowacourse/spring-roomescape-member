// 서버 URL 설정
const BASE_URL = 'http://localhost:8080';

// DOM 로드 완료 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    fetchThemes();
    setDefaultDate();
    setupEventListeners();
});

async function handleResponse(response) {
    if (!response.ok) {
        const errorData = await response.json();
        alert(`[오류 발생]\n${errorData.message}`);
        throw new Error(errorData.message);
    }
    // 204 No Content의 경우 JSON 파싱을 시도하지 않음
    if (response.status === 204) return null;
    return response.json();
}

// 오늘 날짜 기본 세팅
function setDefaultDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('res-date').min = today;
    document.getElementById('res-date').value = today;
}

// 1. 테마 목록 조회 (GET /themes)
async function fetchThemes() {
    try {
        const response = await fetch(`${BASE_URL}/themes`);
        const themes = await handleResponse(response);

        const themeSelect = document.getElementById('res-theme');
        themes.forEach(theme => {
            const option = document.createElement('option');
            option.value = theme.id;
            option.textContent = theme.name;
            themeSelect.appendChild(option);
        });
    } catch (error) {
        console.error("테마 로딩 실패:", error);
    }
}

// 2. 스케줄 조회 (GET /times/{themeId})
async function checkSchedule(isEdit = false) {
    const prefix = isEdit ? 'edit' : 'res';
    const date = document.getElementById(`${prefix}-date`).value;
    const themeId = isEdit ? document.getElementById('edit-theme-id').value : document.getElementById('res-theme').value;

    if (!date || !themeId) {
        alert("날짜와 테마를 모두 선택해주세요.");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/times/${themeId}?date=${date}`);
        const data = await handleResponse(response);

        if (data.schedules && data.schedules.length > 0) {
            data.schedules.sort((a, b) => a.startAt.localeCompare(b.startAt));
        }

        const timeSelect = document.getElementById(`${prefix}-time`);
        timeSelect.innerHTML = '<option value="">시간을 선택하세요</option>';

        let hasAvailableTime = false;

        data.schedules.forEach(schedule => {
            const option = document.createElement('option');
            option.value = schedule.timeId;
            // 예약 가능 여부에 따른 처리
            if (schedule.isAvailable) {
                option.textContent = schedule.startAt;
                hasAvailableTime = true;
            } else {
                option.textContent = `${schedule.startAt} (예약마감)`;
                option.disabled = true;
            }
            timeSelect.appendChild(option);
        });

        if (isEdit) {
            document.getElementById('edit-time-container').classList.remove('hidden');
            document.getElementById('btn-update-reservation').classList.remove('hidden');
        } else {
            document.getElementById('schedule-container').classList.remove('hidden');
            document.getElementById('btn-submit-reservation').classList.remove('hidden');
        }

        if (!hasAvailableTime) {
            alert("선택하신 날짜에 예약 가능한 시간이 없습니다.");
        }

    } catch (error) {
        console.error("스케줄 조회 실패:", error);
    }
}

// 3. 예약 생성 (POST /reservations)
async function submitReservation(e) {
    e.preventDefault();

    const requestData = {
        name: document.getElementById('res-name').value,
        date: document.getElementById('res-date').value,
        timeId: Number(document.getElementById('res-time').value),
        themeId: Number(document.getElementById('res-theme').value)
    };

    if (!requestData.timeId) {
        alert("시간을 선택해주세요.");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/reservations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        await handleResponse(response);
        alert("예약이 성공적으로 완료되었습니다!");

        // 폼 초기화
        document.getElementById('reservation-form').reset();
        document.getElementById('schedule-container').classList.add('hidden');
        document.getElementById('btn-submit-reservation').classList.add('hidden');
        setDefaultDate();

    } catch (error) {
        // handleResponse에서 400 Bad Request 등에 대한 alert 처리 완료됨
        console.error("예약 생성 실패:", error);
    }
}

// 4. 예약 조회 (GET /reservations?name=)
async function searchReservations() {
    const name = document.getElementById('search-name').value.trim();
    if (!name) {
        alert("조회할 예약자 이름을 입력해주세요.");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/reservations?name=${encodeURIComponent(name)}`);
        const reservations = await handleResponse(response);
        renderReservationList(reservations);
    } catch (error) {
        console.error("예약 조회 실패:", error);
    }
}

// 예약 목록 렌더링 함수
function renderReservationList(reservations) {
    const listContainer = document.getElementById('reservation-list');
    listContainer.innerHTML = '';

    if (reservations.length === 0) {
        listContainer.innerHTML = '<p>조회된 예약이 없습니다.</p>';
        return;
    }

    reservations.forEach(res => {
        const item = document.createElement('div');
        item.className = 'reservation-item';
        item.innerHTML = `
            <div class="reservation-info">
                <p><strong>테마:</strong> ${res.theme.name}</p>
                <p><strong>날짜:</strong> ${res.date}</p>
                <p><strong>시간:</strong> ${res.time.startAt}</p>
                <p><strong>예약자:</strong> ${res.name}</p>
            </div>
            <div class="reservation-actions">
                <button class="btn btn-secondary" onclick="openEditModal(${res.id}, '${res.name}', '${res.date}', ${res.theme.id}, '${res.theme.name}')">수정</button>
                <button class="btn btn-danger" onclick="deleteReservation(${res.id}, '${res.name}')">취소</button>
            </div>
        `;
        listContainer.appendChild(item);
    });
}

// 5. 예약 취소 (DELETE /reservations/{id})
async function deleteReservation(id, name) {
    if (!confirm("정말 예약을 취소하시겠습니까?")) return;

    try {
        const response = await fetch(`${BASE_URL}/reservations/${id}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: name })
        });

        await handleResponse(response);
        alert("예약이 취소되었습니다.");
        searchReservations(); // 목록 새로고침
    } catch (error) {
        console.error("예약 취소 실패:", error);
    }
}

// 6. 예약 수정 (PATCH /reservations/{id})
function openEditModal(id, name, date, themeId, themeName) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-name').value = name;
    document.getElementById('edit-date').value = date;
    document.getElementById('edit-theme-id').value = themeId;
    document.getElementById('edit-theme-name').value = themeName;

    document.getElementById('edit-time-container').classList.add('hidden');
    document.getElementById('btn-update-reservation').classList.add('hidden');
    document.getElementById('edit-modal').classList.remove('hidden');
}

async function updateReservation() {
    const id = document.getElementById('edit-id').value;
    const requestData = {
        name: document.getElementById('edit-name').value,
        date: document.getElementById('edit-date').value,
        timeId: Number(document.getElementById('edit-time').value)
    };

    if (!requestData.timeId) {
        alert("변경할 시간을 선택해주세요.");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/reservations/${id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        await handleResponse(response);
        alert("예약이 성공적으로 수정되었습니다.");
        document.getElementById('edit-modal').classList.add('hidden');
        searchReservations(); // 목록 새로고침
    } catch (error) {
        console.error("예약 수정 실패:", error);
    }
}

// 이벤트 리스너 등록
function setupEventListeners() {
    document.getElementById('btn-check-schedule').addEventListener('click', () => checkSchedule(false));
    document.getElementById('reservation-form').addEventListener('submit', submitReservation);
    document.getElementById('btn-search').addEventListener('click', searchReservations);

    // 모달 관련 이벤트
    document.getElementById('btn-close-modal').addEventListener('click', () => {
        document.getElementById('edit-modal').classList.add('hidden');
    });
    document.getElementById('btn-edit-check-schedule').addEventListener('click', () => checkSchedule(true));
    document.getElementById('btn-update-reservation').addEventListener('click', updateReservation);
}
