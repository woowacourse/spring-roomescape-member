<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 예약 조회 - 방탈출 예약</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Noto Sans KR', sans-serif;
            background: #0a0e27;
            min-height: 100vh;
            padding: 40px 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .nav {
            margin-bottom: 40px;
        }

        .back-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 20px;
            background: #151932;
            color: #c5cae9;
            text-decoration: none;
            border-radius: 8px;
            font-size: 0.9rem;
            font-weight: 500;
            border: 1px solid #1f2547;
            transition: all 0.2s ease;
        }

        .back-btn:hover {
            background: #1a1f3a;
            border-color: #2d3561;
            color: #ffffff;
        }

        header {
            text-align: center;
            margin-bottom: 60px;
        }

        h1 {
            font-size: 2.5rem;
            font-weight: 700;
            color: #ffffff;
            margin-bottom: 12px;
            letter-spacing: -0.5px;
        }

        .subtitle {
            font-size: 1rem;
            color: #8b93b0;
            font-weight: 300;
        }

        .search-card {
            background: #151932;
            border: 1px solid #1f2547;
            border-radius: 12px;
            padding: 32px;
            margin-bottom: 40px;
        }

        .search-card h2 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 24px;
        }

        .search-form {
            display: flex;
            gap: 12px;
            align-items: flex-end;
        }

        .form-group {
            flex: 1;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #c5cae9;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 12px 16px;
            background: #0a0e27;
            border: 1px solid #1f2547;
            border-radius: 8px;
            font-size: 0.95rem;
            color: #ffffff;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            background: #0d1129;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 32px;
            border: none;
            border-radius: 8px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
            white-space: nowrap;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }

        .list-card {
            background: #151932;
            border: 1px solid #1f2547;
            border-radius: 12px;
            padding: 32px;
        }

        .list-card h2 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background: #1a1f3a;
            color: #c5cae9;
            padding: 14px;
            text-align: left;
            font-weight: 600;
            font-size: 0.9rem;
            border: 1px solid #1f2547;
        }

        td {
            padding: 14px;
            border: 1px solid #1f2547;
            color: #c5cae9;
            font-size: 0.9rem;
        }

        tr:hover {
            background: #1a1f3a;
        }

        .action-buttons {
            display: flex;
            gap: 8px;
        }

        .btn-edit {
            background: #667eea;
            color: white;
            padding: 6px 14px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            font-size: 0.85rem;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .btn-edit:hover {
            background: #5568d3;
        }

        .btn-delete {
            background: #f5576c;
            color: white;
            padding: 6px 14px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            font-size: 0.85rem;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .btn-delete:hover {
            background: #f34359;
        }

        .empty-state {
            text-align: center;
            color: #5c6686;
            padding: 40px;
            font-size: 0.95rem;
        }

        /* 모달 스타일 */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(10, 14, 39, 0.9);
            overflow-y: auto;
        }

        .modal.active {
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .modal-content {
            background: #151932;
            border: 1px solid #1f2547;
            border-radius: 12px;
            padding: 32px;
            max-width: 600px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            padding-bottom: 16px;
            border-bottom: 1px solid #1f2547;
        }

        .modal-header h3 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
        }

        .close-btn {
            background: none;
            border: none;
            color: #8b93b0;
            font-size: 1.5rem;
            cursor: pointer;
            padding: 0;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: color 0.2s ease;
        }

        .close-btn:hover {
            color: #ffffff;
        }

        .modal .form-group {
            margin-bottom: 20px;
        }

        .modal .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #c5cae9;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .modal .form-group input,
        .modal .form-group select {
            width: 100%;
            padding: 12px 16px;
            background: #0a0e27;
            border: 1px solid #1f2547;
            border-radius: 8px;
            font-size: 0.95rem;
            color: #ffffff;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .modal .form-group input:focus,
        .modal .form-group select:focus {
            outline: none;
            border-color: #667eea;
            background: #0d1129;
        }

        .modal .form-group select option {
            background: #151932;
            color: #ffffff;
        }

        .modal-actions {
            display: flex;
            gap: 12px;
            margin-top: 24px;
        }

        .btn-cancel {
            flex: 1;
            background: #1a1f3a;
            color: #c5cae9;
            padding: 12px 24px;
            border: 1px solid #1f2547;
            border-radius: 8px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .btn-cancel:hover {
            background: #1f2547;
            color: #ffffff;
        }

        .btn-save {
            flex: 1;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .btn-save:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }

        @media (max-width: 768px) {
            h1 {
                font-size: 2rem;
            }

            .search-form {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }

            table {
                font-size: 0.85rem;
            }

            th, td {
                padding: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <nav class="nav">
            <a href="/" class="back-btn">
                <span>←</span>
                <span>메인으로</span>
            </a>
        </nav>

        <header>
            <h1>내 예약 조회</h1>
            <p class="subtitle">이름으로 예약을 검색해보세요</p>
        </header>

        <div class="search-card">
            <h2>예약자 이름 입력</h2>
            <form id="searchForm" class="search-form">
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" id="name" name="name" required placeholder="예약자 이름을 입력하세요">
                </div>
                <button type="submit" class="btn">조회하기</button>
            </form>
        </div>

        <div class="list-card">
            <h2>예약 내역</h2>
            <div id="reservationsList">
                <div class="empty-state">이름을 입력하고 조회 버튼을 눌러주세요.</div>
            </div>
        </div>
    </div>

    <!-- 예약 수정 모달 -->
    <div id="editModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>예약 수정</h3>
                <button class="close-btn" onclick="closeEditModal()">&times;</button>
            </div>
            <form id="editForm">
                <input type="hidden" id="editReservationId">
                <div class="form-group">
                    <label for="editName">이름</label>
                    <input type="text" id="editName" name="name" required readonly>
                </div>
                <div class="form-group">
                    <label for="editDate">날짜</label>
                    <input type="date" id="editDate" name="date" required>
                </div>
                <div class="form-group">
                    <label for="editTimeId">시간</label>
                    <select id="editTimeId" name="timeId" required>
                        <option value="">시간을 선택하세요</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="editThemeId">테마</label>
                    <select id="editThemeId" name="themeId" required>
                        <option value="">테마를 선택하세요</option>
                    </select>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn-cancel" onclick="closeEditModal()">취소</button>
                    <button type="submit" class="btn-save">저장</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // localStorage에서 저장된 사용자 이름을 가져와 입력 필드에 자동으로 채움
            const savedUserName = localStorage.getItem('userName');
            if (savedUserName) {
                document.getElementById('name').value = savedUserName;
                // 자동으로 조회
                loadReservations(savedUserName);
            }
        });

        document.getElementById('searchForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const userName = document.getElementById('name').value.trim();

            if (!userName) {
                alert('이름을 입력해주세요.');
                return;
            }

            // 검색한 이름을 localStorage에 저장
            localStorage.setItem('userName', userName);
            loadReservations(userName);
        });

        function loadReservations(userName) {
            const container = document.getElementById('reservationsList');
            container.innerHTML = '<div class="empty-state">로딩 중...</div>';

            fetch(`/reservations/mine?name=${encodeURIComponent(userName)}`)
                .then(response => response.json())
                .then(reservations => {
                    if (reservations.length === 0) {
                        container.innerHTML = `<div class="empty-state">"${userName}" 님의 예약 내역이 없습니다.</div>`;
                        return;
                    }

                    container.innerHTML = `
                        <table>
                            <thead>
                                <tr>
                                    <th>이름</th>
                                    <th>날짜</th>
                                    <th>시간</th>
                                    <th>테마</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${reservations.map(reservation => `
                                    <tr>
                                        <td>${reservation.name}</td>
                                        <td>${reservation.date}</td>
                                        <td>${reservation.time.startAt}</td>
                                        <td>${reservation.theme.name}</td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn-edit" onclick='openEditModal(${JSON.stringify(reservation)})'>수정</button>
                                                <button class="btn-delete" onclick="deleteReservation(${reservation.id})">취소</button>
                                            </div>
                                        </td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                    `;
                })
                .catch(error => {
                    console.error('예약 목록 불러오기 실패:', error);
                    container.innerHTML = '<div class="empty-state">예약 목록을 불러오는데 실패했습니다.</div>';
                });
        }

        function deleteReservation(id) {
            if (!confirm('정말로 이 예약을 취소하시겠습니까?')) {
                return;
            }

            fetch(`/reservations/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('예약이 취소되었습니다.');
                    // 현재 입력된 이름으로 다시 조회
                    const userName = document.getElementById('name').value;
                    loadReservations(userName);
                } else {
                    return response.json().then(error => {
                        alert('예약 취소 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('예약 취소 실패:', error);
                alert('예약 취소 중 오류가 발생했습니다.');
            });
        }

        // 수정 모달 열기
        function openEditModal(reservation) {
            document.getElementById('editReservationId').value = reservation.id;
            document.getElementById('editName').value = reservation.name;
            document.getElementById('editDate').value = reservation.date;

            // 시간과 테마 데이터 로드
            loadTimesForEdit(reservation.time.id);
            loadThemesForEdit(reservation.theme.id);

            document.getElementById('editModal').classList.add('active');
        }

        // 수정 모달 닫기
        function closeEditModal() {
            document.getElementById('editModal').classList.remove('active');
            document.getElementById('editForm').reset();
        }

        // 모달 외부 클릭 시 닫기
        document.getElementById('editModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeEditModal();
            }
        });

        // 시간 목록 로드 (수정용)
        function loadTimesForEdit(selectedTimeId) {
            fetch('/times')
                .then(response => response.json())
                .then(times => {
                    const select = document.getElementById('editTimeId');
                    select.innerHTML = '<option value="">시간을 선택하세요</option>';
                    times.forEach(time => {
                        const option = document.createElement('option');
                        option.value = time.id;
                        option.textContent = time.startAt;
                        if (time.id === selectedTimeId) {
                            option.selected = true;
                        }
                        select.appendChild(option);
                    });
                })
                .catch(error => console.error('시간 목록 불러오기 실패:', error));
        }

        // 테마 목록 로드 (수정용)
        function loadThemesForEdit(selectedThemeId) {
            fetch('/themes')
                .then(response => response.json())
                .then(themes => {
                    const select = document.getElementById('editThemeId');
                    select.innerHTML = '<option value="">테마를 선택하세요</option>';
                    themes.forEach(theme => {
                        const option = document.createElement('option');
                        option.value = theme.id;
                        option.textContent = theme.name;
                        if (theme.id === selectedThemeId) {
                            option.selected = true;
                        }
                        select.appendChild(option);
                    });
                })
                .catch(error => console.error('테마 목록 불러오기 실패:', error));
        }

        // 예약 수정 폼 제출
        document.getElementById('editForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const reservationId = document.getElementById('editReservationId').value;
            const updateData = {
                name: document.getElementById('editName').value,
                date: document.getElementById('editDate').value,
                timeId: parseInt(document.getElementById('editTimeId').value),
                themeId: parseInt(document.getElementById('editThemeId').value)
            };

            fetch(`/reservations/${reservationId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updateData)
            })
            .then(response => {
                if (response.ok) {
                    alert('예약이 수정되었습니다!');
                    closeEditModal();
                    // 현재 입력된 이름으로 다시 조회
                    const userName = document.getElementById('name').value;
                    loadReservations(userName);
                } else {
                    return response.json().then(error => {
                        alert('예약 수정 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('예약 수정 실패:', error);
                alert('예약 수정 중 오류가 발생했습니다.');
            });
        });
    </script>
</body>
</html>
