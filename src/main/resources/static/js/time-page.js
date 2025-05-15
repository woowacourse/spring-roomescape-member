let isEditing = false;
const API_ENDPOINT = '/times';
const cellFields = ['id', 'startAt'];
const createCellFields = ['', createInput()];

// 페이지네이션 관련 변수 추가
let currentPage = 1;
let totalPages = 1;

function createBody(inputs) {
    return {
        startAt: inputs[0].value,
    };
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('add-button').addEventListener('click', addRow);

    // URL에서 페이지 번호 확인
    const urlParams = new URLSearchParams(window.location.search);
    currentPage = parseInt(urlParams.get('page')) || 1;

    // 페이지 데이터 로드
    loadTimes(currentPage);
});

// 시간 데이터 로드 함수
function loadTimes(page) {
    // URL 쿼리 파라미터를 통해 페이지 정보 전달
    fetch(`/admin${API_ENDPOINT}?page=${page}`)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        })
        .then(data => {
            // 백엔드 응답 구조에 따라 처리
            render(data.reservationTimes || []);
            if (data.totalPages) {
                // 페이지네이션 정보가 포함된 응답
                totalPages = data.totalPages;
            } else {
                totalPages = Math.max(1, Math.ceil(data.length / 10));
            }

            // 페이지네이션 렌더링
            renderPagination();
        })
        .catch(error => console.error('Error fetching times:', error));
}

// 페이지네이션 UI 렌더링 함수
function renderPagination() {
    const paginationEl = document.getElementById('pagination');
    if (!paginationEl) {
        // 페이지네이션 컨테이너가 없으면 생성
        const tableContainer = document.querySelector('.table-container') || document.querySelector('table').parentNode;

        const paginationContainer = document.createElement('div');
        paginationContainer.className = 'pagination-container mt-3';

        const nav = document.createElement('nav');
        nav.setAttribute('aria-label', 'Time pagination');

        const ul = document.createElement('ul');
        ul.className = 'pagination justify-content-center';
        ul.id = 'pagination';

        nav.appendChild(ul);
        paginationContainer.appendChild(nav);
        tableContainer.appendChild(paginationContainer);

        createPaginationButtons(ul);
    } else {
        createPaginationButtons(paginationEl);
    }
}

// 페이지네이션 버튼 생성 함수
function createPaginationButtons(container) {
    container.innerHTML = '';

    // 이전 페이지 버튼
    const prevBtn = document.createElement('li');
    prevBtn.className = `page-item ${currentPage <= 1 ? 'disabled' : ''}`;

    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = `?page=${currentPage - 1}`;
    prevLink.textContent = '이전';
    if (currentPage <= 1) {
        prevLink.addEventListener('click', e => e.preventDefault());
    }

    prevBtn.appendChild(prevLink);
    container.appendChild(prevBtn);

    // 페이지 번호 버튼
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    // 범위 조정
    if (endPage - startPage + 1 < maxVisiblePages && startPage > 1) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageItem = document.createElement('li');
        pageItem.className = `page-item ${i === currentPage ? 'active' : ''}`;

        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = `?page=${i}`;
        pageLink.textContent = i;

        pageItem.appendChild(pageLink);
        container.appendChild(pageItem);
    }

    // 다음 페이지 버튼
    const nextBtn = document.createElement('li');
    nextBtn.className = `page-item ${currentPage >= totalPages ? 'disabled' : ''}`;

    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = `?page=${currentPage + 1}`;
    nextLink.textContent = '다음';
    if (currentPage >= totalPages) {
        nextLink.addEventListener('click', e => e.preventDefault());
    }

    nextBtn.appendChild(nextLink);
    container.appendChild(nextBtn);
}

function render(data) {
    const tableBody = document.getElementById('table-body');
    tableBody.innerHTML = '';

    data.forEach(item => {
        const row = tableBody.insertRow();

        cellFields.forEach((field, index) => {
            row.insertCell(index).textContent = item[field];
        });

        const actionCell = row.insertCell(row.cells.length);
        actionCell.appendChild(createActionButton('삭제', 'btn-danger', deleteRow));
    });
}

function addRow() {
    if (isEditing) return;  // 이미 편집 중인 경우 추가하지 않음

    const tableBody = document.getElementById('table-body');
    const row = tableBody.insertRow();
    isEditing = true;

    createAddField(row);
}

function createAddField(row) {
    createCellFields.forEach((field, index) => {
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

function createInput() {
    const input = document.createElement('input');
    input.type = 'time'
    input.className = 'form-control';
    return input;
}

function createActionButton(label, className, eventListener) {
    const button = document.createElement('button');
    button.textContent = label;
    button.classList.add('btn', className, 'mr-2');
    button.addEventListener('click', eventListener);
    return button;
}

function saveRow(event) {
    const row = event.target.parentNode.parentNode;
    const inputs = row.querySelectorAll('input');
    const body = createBody(inputs);

    requestCreate(body)
        .then(() => {
            // 페이지 새로고침 대신 현재 페이지 다시 로드
            loadTimes(currentPage);
            isEditing = false;
        })
        .catch(error => console.error('Error:', error));
}

function deleteRow(event) {
    const row = event.target.closest('tr');
    const id = row.cells[0].textContent;

    requestDelete(id)
        .then(() => {
            // 항목 삭제 후 현재 페이지 다시 로드
            // 마지막 항목을 삭제한 경우 이전 페이지로 이동할 수 있음
            const remainingRows = document.querySelectorAll('#table-body tr').length;
            if (remainingRows <= 1 && currentPage > 1) {
                loadTimes(currentPage - 1);
            } else {
                loadTimes(currentPage);
            }
        })
        .catch(error => console.error('Error:', error));
}

// request 함수

function requestCreate(data) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    };

    return fetch(API_ENDPOINT, requestOptions)
        .then(response => {
            if (response.status === 201) return response.json();
            throw new Error('Create failed');
        });
}

function requestRead() {
    return fetch(API_ENDPOINT)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        });
}

function requestDelete(id) {
    const requestOptions = {
        method: 'DELETE',
    };

    return fetch(`${API_ENDPOINT}/${id}`, requestOptions)
        .then(response => {
            if (response.status !== 204) throw new Error('Delete failed');
        });
}