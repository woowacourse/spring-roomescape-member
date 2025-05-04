let isEditing = false;
const API_ENDPOINT = '/times';
const cellFields = ['id', 'startAt'];
const createCellFields = ['', createInput()];
function createBody(inputs) {
  return {
    startAt: inputs[0].value,
  };
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('add-button').addEventListener('click', addRow);
  requestRead()
      .then(render)
      .catch(error => console.error(error.message));
});

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
        location.reload();
      })
      .catch(error => console.error(error.message));

  isEditing = false;  // isEditing 값을 false로 설정
}

function deleteRow(event) {
  const row = event.target.closest('tr');
  const id = row.cells[0].textContent;

  requestDelete(id)
      .then(() => row.remove())
      .catch(error => console.error(error.message));
}


// request

async function requestCreate(data) {
  const requestOptions = {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data)
  };

  const response = await fetch(API_ENDPOINT, requestOptions);
  if (response.status === 201)
      return await response.json();
  throw new Error(await response.text());
}

async function requestRead() {
  const response = await fetch(API_ENDPOINT);
  if (response.status === 200)
      return await response.json();
  throw new Error(await response.text());
}

async function requestDelete(id) {
  const requestOptions = {
    method: 'DELETE',
  };

  const response = await fetch(`${API_ENDPOINT}/${id}`, requestOptions);
  if (response.status !== 204)
      throw new Error(await response.text());
}
