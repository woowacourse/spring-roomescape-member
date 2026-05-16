const BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 모든 기초 데이터 초기화
    loadAllData();
    setupAdminEventListeners();
});

// 전역 에러 핸들링 함수
async function handleAdminResponse(response) {
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({ message: "알 수 없는 서버 오류가 발생했습니다." }));
        alert(`[관리자 요청 오류]\n${errorData.message}`);
        throw new Error(errorData.message);
    }
    if (response.status === 204) return null;
    return response.json();
}

// ==========================================
// 1. 데이터 로딩 (Read)
// ==========================================
async function loadAllData() {
    await fetchAdminThemes();
    await fetchAdminTimes();
    await fetchAllReservations();
}

// 테마 목록 로딩 (GET /themes)
async function fetchAdminThemes() {
    try {
        const response = await fetch(`${BASE_URL}/themes`);
        const themes = await handleAdminResponse(response);

        const listContainer = document.getElementById('admin-theme-list');
        const themeSelect = document.getElementById('admin-res-theme');

        listContainer.innerHTML = '';
        themeSelect.innerHTML = '<option value="">테마 선택</option>';

        themes.forEach(theme => {
            // 리스트 렌더링
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <span><strong>${theme.name}</strong> (${theme.description})</span>
                <button class="btn btn-danger btn-sm" onclick="deleteTheme(${theme.id})">삭제</button>
            `;
            listContainer.appendChild(div);

            // 셀렉트 박스 렌더링
            const option = document.createElement('option');
            option.value = theme.id;
            option.textContent = theme.name;
            themeSelect.appendChild(option);
        });
    } catch (error) { console.error("테마 로딩 실패:", error); }
}

// 예약 시간 목록 로딩 (GET /times)
async function fetchAdminTimes() {
    try {
        const response = await fetch(`${BASE_URL}/times`);
        const times = await handleAdminResponse(response);

        const listContainer = document.getElementById('admin-time-list');
        const timeSelect = document.getElementById('admin-res-time');

        listContainer.innerHTML = '';
        timeSelect.innerHTML = '<option value="">시간 선택</option>';

        times.forEach(time => {
            // 리스트 렌더링
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <span><span class="badge">ID: ${time.id}</span> ${time.startAt}</span>
                <button class="btn btn-danger btn-sm" onclick="deleteTime(${time.id})">삭제</button>
            `;
            listContainer.appendChild(div);

            // 셀렉트 박스 렌더링
            const option = document.createElement('option');
            option.value = time.id;
            option.textContent = time.startAt;
            timeSelect.appendChild(option);
        });
    } catch (error) { console.error("시간 로딩 실패:", error); }
}

// 전체 예약 목록 로딩 (GET /admin/reservations)
async function fetchAllReservations() {
    try {
        const response = await fetch(`${BASE_URL}/admin/reservations`);
        const reservations = await handleAdminResponse(response);

        const listContainer = document.getElementById('admin-reservation-list');
        listContainer.innerHTML = '';

        if (reservations.length === 0) {
            listContainer.innerHTML = '<p style="text-align:center; color:#7f8c8d;">등록된 예약이 없습니다.</p>';
            return;
        }

        reservations.forEach(res => {
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <div>
                    <strong>[${res.date}] ${res.time.startAt}</strong><br>
                    <span>테마: ${res.theme.name} | 예약자: <span style="color:var(--primary-color); font-weight:bold;">${res.name}</span></span>
                </div>
                <button class="btn btn-danger" onclick="deleteAdminReservation(${res.id})">강제 취소</button>
            `;
            listContainer.appendChild(div);
        });
    } catch (error) { console.error("전체 예약 로딩 실패:", error); }
}

// ==========================================
// 2. 데이터 추가 (Create)
// ==========================================
async function addTheme(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('theme-name').value,
        description: document.getElementById('theme-desc').value,
        thumbnail: document.getElementById('theme-thumb').value
    };

    try {
        const response = await fetch(`${BASE_URL}/admin/themes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        await handleAdminResponse(response);
        alert("테마가 추가되었습니다.");
        document.getElementById('form-add-theme').reset();
        fetchAdminThemes(); // 갱신
    } catch (error) {}
}

async function addTime(e) {
    e.preventDefault();
    let timeValue = document.getElementById('time-start').value;
    // HH:mm 형식으로 들어오면 초(ss)를 붙여줍니다.
    if (timeValue.length === 5) {
        timeValue += ":00";
    }

    try {
        const response = await fetch(`${BASE_URL}/admin/times`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ startAt: timeValue })
        });
        await handleAdminResponse(response);
        alert("시간이 추가되었습니다.");
        document.getElementById('form-add-time').reset();
        fetchAdminTimes(); // 갱신
    } catch (error) {}
}

async function addAdminReservation(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('admin-res-name').value,
        date: document.getElementById('admin-res-date').value,
        themeId: Number(document.getElementById('admin-res-theme').value),
        timeId: Number(document.getElementById('admin-res-time').value)
    };

    try {
        const response = await fetch(`${BASE_URL}/admin/reservations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        await handleAdminResponse(response);
        alert("관리자 권한으로 예약이 강제 생성되었습니다.");
        document.getElementById('form-admin-reservation').reset();
        fetchAllReservations(); // 갱신
    } catch (error) {}
}

// ==========================================
// 3. 데이터 삭제 (Delete)
// ==========================================
async function deleteTheme(id) {
    if (!confirm("정말 이 테마를 삭제하시겠습니까?")) return;
    try {
        const response = await fetch(`${BASE_URL}/admin/themes/${id}`, { method: 'DELETE' });
        await handleAdminResponse(response);
        alert("테마가 삭제되었습니다.");
        fetchAdminThemes();
    } catch (error) {}
}

async function deleteTime(id) {
    if (!confirm("정말 이 예약 시간을 삭제하시겠습니까?\n(예약 내역이 존재하면 충돌(Conflict)이 발생할 수 있습니다.)")) return;
    try {
        const response = await fetch(`${BASE_URL}/admin/times/${id}`, { method: 'DELETE' });
        await handleAdminResponse(response);
        alert("시간이 삭제되었습니다.");
        fetchAdminTimes();
    } catch (error) {}
}

async function deleteAdminReservation(id) {
    if (!confirm("관리자 권한으로 이 예약을 강제로 취소하시겠습니까?")) return;
    try {
        const response = await fetch(`${BASE_URL}/admin/reservations/${id}`, { method: 'DELETE' });
        await handleAdminResponse(response);
        alert("예약이 강제 취소되었습니다.");
        fetchAllReservations();
    } catch (error) {}
}

// ==========================================
// 4. 이벤트 리스너 등록
// ==========================================
function setupAdminEventListeners() {
    document.getElementById('form-add-theme').addEventListener('submit', addTheme);
    document.getElementById('form-add-time').addEventListener('submit', addTime);
    document.getElementById('form-admin-reservation').addEventListener('submit', addAdminReservation);
}
