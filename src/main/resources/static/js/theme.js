let isEditing = false;
const THEME_API = '/admin/theme';

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('add-theme').addEventListener('click', addEditableRow);
  fetchThemes();
});

function fetchThemes() {
  fetch(THEME_API)
    .then(res => res.json())
    .then(renderThemes)
    .catch(err => console.error('테마 조회 실패:', err));
}

function renderThemes(data) {
  const tableBody = document.getElementById('theme-table-body');
  tableBody.innerHTML = '';
  data.forEach(theme => {
    const row = tableBody.insertRow();
    insertThemeRow(row, theme);
  });
}

function insertThemeRow(row, theme) {
  row.insertCell(0).textContent = theme.id;
  row.insertCell(1).textContent = theme.name;
  row.insertCell(2).textContent = theme.description;

  const imgCell = row.insertCell(3);
  if (theme.imageUrl) {
    const img = document.createElement('img');
    img.src = theme.imageUrl;
    img.className = 'theme-thumbnail';
    imgCell.appendChild(img);
  }

  const actionCell = row.insertCell(4);
  actionCell.appendChild(createButton('삭제', 'btn-danger', deleteRow));
}

function addEditableRow() {
  if (isEditing) return;
  isEditing = true;

  const tableBody = document.getElementById('theme-table-body');
  const row = tableBody.insertRow();

  row.insertCell(0).textContent = '';

  const nameInput = createTextInput('이름');
  row.insertCell(1).appendChild(nameInput);

  const descInput = createTextInput('설명');
  row.insertCell(2).appendChild(descInput);

  const imageInput = createTextInput('이미지 URL');
  row.insertCell(3).appendChild(imageInput);

  const actionCell = row.insertCell(4);
  actionCell.appendChild(createButton('확인', 'btn-primary mr-1', () => saveRow(row, nameInput, descInput, imageInput)));
  actionCell.appendChild(createButton('취소', 'btn-secondary', () => { row.remove(); isEditing = false; }));
}

function saveRow(row, nameInput, descInput, imageInput) {
  const body = {
    name: nameInput.value.trim(),
    description: descInput.value.trim(),
    imageUrl: imageInput.value.trim()
  };

  if (!body.name || !body.description) {
    alert('이름과 설명을 입력해주세요.');
    return;
  }

  fetch(THEME_API, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(body)
  })
    .then(res => {
      if (res.status === 200) return res.json();
      throw new Error('테마 추가 실패');
    })
    .then(data => {
      row.cells[0].textContent = data.id;
      row.cells[1].innerHTML = '';
      row.cells[1].textContent = data.name;
      row.cells[2].innerHTML = '';
      row.cells[2].textContent = data.description;
      row.cells[3].innerHTML = '';
      if (data.imageUrl) {
        const img = document.createElement('img');
        img.src = data.imageUrl;
        img.className = 'theme-thumbnail';
        row.cells[3].appendChild(img);
      }
      row.cells[4].innerHTML = '';
      row.cells[4].appendChild(createButton('삭제', 'btn-danger', deleteRow));
      isEditing = false;
    })
    .catch(err => alert(err.message));
}

function deleteRow(event) {
  const row = event.target.closest('tr');
  const id = row.cells[0].textContent;
  fetch(`${THEME_API}/${id}`, { method: 'DELETE' })
    .then(res => { if (res.status === 204) row.remove(); })
    .catch(err => console.error('삭제 실패:', err));
}

function createTextInput(placeholder) {
  const input = document.createElement('input');
  input.type = 'text';
  input.placeholder = placeholder;
  input.className = 'form-control';
  return input;
}

function createButton(label, className, handler) {
  const btn = document.createElement('button');
  btn.textContent = label;
  btn.className = `btn ${className}`;
  btn.addEventListener('click', handler);
  return btn;
}