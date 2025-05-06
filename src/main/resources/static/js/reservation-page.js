let isEditing = false;
const RESERVATION_API_ENDPOINT = '/reservations';
const TIME_API_ENDPOINT = '/times';
const THEME_API_ENDPOINT = '/themes';
const timesOptions = [];
const themesOptions = [];

// 페이지 관련 상태 변수
let currentPage = 1;
let totalPages = 1;

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('add-button').addEventListener('click', addInputRow);

    // URL에서 페이지 번호 확인
    const urlParams = new URLSearchParams(window.location.search);
    currentPage = parseInt(urlParams.get('page')) || 1;

    // 페이지 데이터 로드
    loadReservations(currentPage);

    fetchTimes();
    fetchThemes();
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
            render(data.reservations || []);

            // 페이지네이션 정보 설정
            if (data.totalPages) {
                totalPages = data.totalPages;
            } else {
                // 백엔드가 페이지네이션 정보를 제공하지 않는 경우 기본값 설정
                totalPages = Math.max(1, Math.ceil(data.length / 10));
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

        row.insertCell(0).textContent = item.id;            // 예약 id
        row.insertCell(1).textContent = item.name;          // 예약자명
        row.insertCell(2).textContent = item.theme.name;    // 테마명
        row.insertCell(3).textContent = item.date;          // 예약 날짜
        row.insertCell(4).textContent = item.time.startAt;  // 시작 시간

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
        })
        .catch(error => console.error('Error fetching theme:', error));
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

    const nameInput = createInput('text');
    const dateInput = createInput('date');
    const timeDropdown = createSelect(timesOptions, "시간 선택", 'time-select', 'startAt');
    const themeDropdown = createSelect(themesOptions, "테마 선택", 'theme-select', 'name');

    const cellFieldsToCreate = ['', nameInput, themeDropdown, dateInput, timeDropdown];

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
    const nameInput = row.querySelector('input[type="text"]');
    const themeSelect = row.querySelector('#theme-select');
    const dateInput = row.querySelector('input[type="date"]');
    const timeSelect = row.querySelector('#time-select');

    const reservation = {
        name: nameInput.value,
        themeId: themeSelect.value,
        date: dateInput.value,
        timeId: timeSelect.value
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

function requestCreate(reservation) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reservation)
    };

    return fetch(RESERVATION_API_ENDPOINT, requestOptions)
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
