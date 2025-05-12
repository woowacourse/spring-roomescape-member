let isEditing = false;
const RESERVATION_API_ENDPOINT = '/admin/reservations';
const TIME_API_ENDPOINT = '/times/all';
const THEME_API_ENDPOINT = '/themes/all';
const MEMBER_API_ENDPOINT = '/admin/users/all';
const timesOptions = [];
const themesOptions = [];
const membersOptions = [];

// 페이지 관련 상태 변수
let currentPage = 1;
let totalPages = 1;

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('add-button').addEventListener('click', addInputRow);
    document.getElementById('filter-form').addEventListener('submit', applyFilter);

    // URL에서 페이지 번호 확인
    const urlParams = new URLSearchParams(window.location.search);
    currentPage = parseInt(urlParams.get('page')) || 1;

    // 페이지 데이터 로드
    loadReservations(currentPage);

    fetchTimes();
    fetchThemes();
    fetchMembers();
});

function loadReservations(page) {
    // URL 쿼리 파라미터를 통해 페이지 정보 전달
    fetch(`${RESERVATION_API_ENDPOINT}?page=${page}`)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        })
        .then(data => {
            // 데이터 렌더링
            if (Array.isArray(data)) {
                // 배열 형태로 응답이 올 경우
                render(data);
                // 백엔드가 페이지네이션 정보를 제공하지 않는 경우 기본값 설정
                totalPages = Math.max(1, Math.ceil(data.length / 10));
            } else {
                // 페이지네이션 정보가 포함된 객체 형태로 응답이 올 경우
                render(data.reservations || []);
                if (data.totalPages) {
                    totalPages = data.totalPages;
                }
            }

            // 페이지네이션 렌더링
            renderPagination();
        })
        .catch(error => console.error('Error fetching reservations:', error));
}

function renderPagination() {
    const paginationEl = document.getElementById('pagination');
    if (!paginationEl) return;

    paginationEl.innerHTML = '';

    // 이전 페이지 버튼
    const prevBtn = document.createElement('li');
    prevBtn.className = `page-item ${currentPage <= 1 ? 'disabled' : ''}`;

    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = currentPage > 1 ? `?page=${currentPage - 1}` : '#';
    prevLink.textContent = '이전';
    prevBtn.appendChild(prevLink);
    paginationEl.appendChild(prevBtn);

    // 페이지 번호 버튼들
    // 최대 5개의 페이지 번호만 표시
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, startPage + 4);

    for (let i = startPage; i <= endPage; i++) {
        const pageItem = document.createElement('li');
        pageItem.className = `page-item ${i === currentPage ? 'active' : ''}`;

        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = `?page=${i}`;
        pageLink.textContent = i;

        pageItem.appendChild(pageLink);
        paginationEl.appendChild(pageItem);
    }

    // 다음 페이지 버튼
    const nextBtn = document.createElement('li');
    nextBtn.className = `page-item ${currentPage >= totalPages ? 'disabled' : ''}`;

    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = currentPage < totalPages ? `?page=${currentPage + 1}` : '#';
    nextLink.textContent = '다음';
    nextBtn.appendChild(nextLink);
    paginationEl.appendChild(nextBtn);
}

function render(data) {
    const tableBody = document.getElementById('table-body');
    tableBody.innerHTML = '';

    data.forEach(item => {
        const row = tableBody.insertRow();

        /*
        TODO: [5단계] 예약 생성 기능 변경 - 관리자
              예약 목록 조회 API 응답에 맞게 적용
        */
        row.insertCell(0).textContent = item.id;              // 예약 id
        row.insertCell(1).textContent = item.member.name;     // 사용자 name
        row.insertCell(2).textContent = item.theme.name;      // 테마 name
        row.insertCell(3).textContent = item.date;            // date
        row.insertCell(4).textContent = item.time.startAt;    // 예약 시간 startAt

        const actionCell = row.insertCell(row.cells.length);
        actionCell.appendChild(createActionButton('삭제', 'btn-danger', deleteRow));
    });
}

function fetchTimes() {
    requestRead(TIME_API_ENDPOINT)
        .then(data => {
            timesOptions.push(...data);
        })
        .catch(error => console.error('Error fetching time:', error));
}

function fetchThemes() {
    requestRead(THEME_API_ENDPOINT)
        .then(data => {
            themesOptions.push(...data);
            populateSelect('theme', themesOptions, 'name');
        })
        .catch(error => console.error('Error fetching theme:', error));
}

function fetchMembers() {
    requestRead(MEMBER_API_ENDPOINT)
        .then(data => {
            membersOptions.push(...data);
            populateSelect('member', membersOptions, 'name');
        })
        .catch(error => console.error('Error fetching member:', error));
}

function populateSelect(selectId, options, textProperty) {
    const select = document.getElementById(selectId);
    if (!select) return;

    options.forEach(optionData => {
        const option = document.createElement('option');
        option.value = optionData.id;
        option.textContent = optionData[textProperty];
        select.appendChild(option);
    });
}

function createSelect(options, defaultText, selectId, textProperty) {
    const select = document.createElement('select');
    select.className = 'form-control';
    select.id = selectId;

    // 기본 옵션 추가
    const defaultOption = document.createElement('option');
    defaultOption.textContent = defaultText;
    select.appendChild(defaultOption);

    // 넘겨받은 옵션을 바탕으로 드롭다운 메뉴 아이템 생성
    options.forEach(optionData => {
        const option = document.createElement('option');
        option.value = optionData.id;
        option.textContent = optionData[textProperty]; // 동적 속성 접근
        select.appendChild(option);
    });

    return select;
}

function createActionButton(label, className, eventListener) {
    const button = document.createElement('button');
    button.textContent = label;
    button.classList.add('btn', className, 'mr-2');
    button.addEventListener('click', eventListener);
    return button;
}

function addInputRow() {
    if (isEditing) return;  // 이미 편집 중인 경우 추가하지 않음

    const tableBody = document.getElementById('table-body');
    const row = tableBody.insertRow();
    isEditing = true;

    const dateInput = createInput('date');
    const timeDropdown = createSelect(timesOptions, "시간 선택", 'time-select', 'startAt');
    const themeDropdown = createSelect(themesOptions, "테마 선택", 'theme-select', 'name');
    const memberDropdown = createSelect(membersOptions, "멤버 선택", 'member-select', 'name');

    const cellFieldsToCreate = ['', memberDropdown, themeDropdown, dateInput, timeDropdown];

    cellFieldsToCreate.forEach((field, index) => {
        const cell = row.insertCell(index);
        if (typeof field === 'string') {
            cell.textContent = field;
        } else {
            cell.appendChild(field);
        }
    });

    const actionCell = row.insertCell(row.cells.length);
    actionCell.appendChild(createActionButton('확인', 'btn-custom', saveRow));
    actionCell.appendChild(createActionButton('취소', 'btn-secondary', () => {
        row.remove();
        isEditing = false;
    }));
}

function createInput(type) {
    const input = document.createElement('input');
    input.type = type;
    input.className = 'form-control';
    return input;
}

function saveRow(event) {
    // 이벤트 전파를 막는다
    event.stopPropagation();

    const row = event.target.parentNode.parentNode;
    const dateInput = row.querySelector('input[type="date"]');
    const memberSelect = row.querySelector('#member-select');
    const themeSelect = row.querySelector('#theme-select');
    const timeSelect = row.querySelector('#time-select');

    const reservation = {
        date: dateInput.value,
        themeId: themeSelect.value,
        timeId: timeSelect.value,
        memberId: memberSelect.value,
    };

    requestCreate(reservation)
        .then(() => {
            // 현재 페이지 다시 로드
            loadReservations(currentPage);
            isEditing = false;
        })
        .catch(error => console.error('Error:', error));
}

function deleteRow(event) {
    const row = event.target.closest('tr');
    const reservationId = row.cells[0].textContent;

    requestDelete(reservationId)
        .then(() => {
            // 성공적으로 삭제한 후 현재 페이지 다시 로드
            loadReservations(currentPage);
        })
        .catch(error => console.error('Error:', error));
}

function applyFilter(event) {
    event.preventDefault();

    const themeId = document.getElementById('theme').value;
    const memberId = document.getElementById('member').value;
    const dateFrom = document.getElementById('date-from').value;
    const dateTo = document.getElementById('date-to').value;

    /*
    TODO: [6단계] 예약 검색 - 조건에 따른 예약 조회 API 호출
          요청 포맷에 맞게 설정
    */
    // 필터 조건 쿼리 매개변수 구성
    let queryParams = new URLSearchParams();
    if (themeId) queryParams.append('themeId', themeId);
    if (memberId) queryParams.append('userId', memberId);
    if (dateFrom) queryParams.append('dateFrom', dateFrom);
    if (dateTo) queryParams.append('dateTo', dateTo);
    queryParams.append('page', currentPage); // 현재 페이지 정보 추가
    fetch(`${RESERVATION_API_ENDPOINT}?${queryParams.toString()}`, { // 예약 검색 API 호출
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(response => {
        if (response.status === 200) return response.json();
        throw new Error('Read failed');
    }).then(data => {
        // 데이터 렌더링 및 페이지네이션 처리
        if (Array.isArray(data)) {
            render(data);
            totalPages = Math.max(1, Math.ceil(data.length / 10));
        } else {
            render(data.reservations || []);
            if (data.totalPages) {
                totalPages = data.totalPages;
            }
        }
        renderPagination();
    })
        .catch(error => console.error("Error fetching available times:", error));
}

function requestCreate(reservation) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reservation)
    };

    return fetch('/admin/reservations', requestOptions)
        .then(response => {
            if (response.status === 201) return response.json();
            throw new Error('Create failed');
        });
}

function requestDelete(id) {
    const requestOptions = {
        method: 'DELETE',
    };

    return fetch(`${RESERVATION_API_ENDPOINT}/${id}`, requestOptions)
        .then(response => {
            if (response.status !== 204) throw new Error('Delete failed');
        });
}

function requestRead(endpoint) {
    return fetch(endpoint)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        });
}
