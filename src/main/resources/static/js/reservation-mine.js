document.addEventListener('DOMContentLoaded', () => {
  /*
  TODO: [2단계] 내 예약 목록 조회 기능
        endpoint 설정
   */
  fetch('') // 내 예약 목록 조회 API 호출
      .then(response => {
        if (response.status === 200) return response.json();
        throw new Error('Read failed');
      })
      .then(render)
      .catch(error => console.error('Error fetching reservations:', error));
});

function render(data) {
  const tableBody = document.getElementById('table-body');
  tableBody.innerHTML = '';

  data.forEach(item => {
    const row = tableBody.insertRow();

    /*
    TODO: [2단계] 내 예약 목록 조회 기능
          response 명세에 맞춰 값 설정
     */
    const theme = '';
    const date = '';
    const time = '';
    const status = '';

    row.insertCell(0).textContent = theme;
    row.insertCell(1).textContent = date;
    row.insertCell(2).textContent = time;
    row.insertCell(3).textContent = status;

    /*
    TODO: [3단계] 예약 대기 기능 - 예약 대기 취소 기능 구현 후 활성화
     */
    if (status !== '예약') { // 예약 대기 상태일 때 예약 대기 취소 버튼 추가하는 코드, 상태 값은 변경 가능
      const cancelCell = row.insertCell(4);
      const cancelButton = document.createElement('button');
      cancelButton.textContent = '취소';
      cancelButton.className = 'btn btn-danger';
      cancelButton.onclick = function () {
        requestDeleteWaiting(item.id).then(() => window.location.reload());
      };
      cancelCell.appendChild(cancelButton);
    } else { // 예약 완료 상태일 때
      row.insertCell(4).textContent = '';
    }
  });
}

function requestDeleteWaiting(id) {
  /*
  TODO: [3단계] 예약 대기 기능 - 예약 대기 취소 API 호출
   */
  const endpoint = '';
  return fetch(endpoint, {
    method: 'DELETE'
  }).then(response => {
    if (response.status === 204) return;
    throw new Error('Delete failed');
  });
}
