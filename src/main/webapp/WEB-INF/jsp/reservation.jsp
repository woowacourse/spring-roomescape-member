<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>방탈출 예약</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        h1 {
            color: white;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
        }

        .form-section {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            margin-bottom: 30px;
        }

        .form-section h2 {
            color: #667eea;
            margin-bottom: 20px;
            font-size: 1.5em;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 600;
        }

        .form-group input, .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            transition: border-color 0.3s;
        }

        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .reservations-list {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .reservations-list h2 {
            color: #667eea;
            margin-bottom: 20px;
            font-size: 1.5em;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background: #667eea;
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: 600;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #e0e0e0;
        }

        tr:hover {
            background: #f8f9fa;
        }

        .btn-delete {
            background: #e74c3c;
            color: white;
            padding: 6px 15px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.3s;
        }

        .btn-delete:hover {
            background: #c0392b;
        }

        .empty-message {
            text-align: center;
            color: #999;
            padding: 40px;
            font-size: 1.1em;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>방탈출 예약</h1>

        <div class="form-section">
            <h2>새 예약 만들기</h2>
            <form id="reservationForm">
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" id="name" name="name" required placeholder="이름을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="date">날짜</label>
                    <input type="date" id="date" name="date" required>
                </div>
                <div class="form-group">
                    <label for="timeId">시간</label>
                    <select id="timeId" name="timeId" required>
                        <option value="">시간을 선택하세요</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="themeId">테마</label>
                    <select id="themeId" name="themeId" required>
                        <option value="">테마를 선택하세요</option>
                    </select>
                </div>
                <button type="submit" class="btn">예약하기</button>
            </form>
        </div>

        <div class="reservations-list">
            <h2>내 예약 목록</h2>
            <div id="reservationsList">
                <div class="empty-message">로딩 중...</div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            loadTimes();
            loadThemes();
            loadReservations();
        });

        function loadTimes() {
            fetch('/times')
                .then(response => response.json())
                .then(times => {
                    const select = document.getElementById('timeId');
                    times.forEach(time => {
                        const option = document.createElement('option');
                        option.value = time.id;
                        option.textContent = time.startAt;
                        select.appendChild(option);
                    });
                })
                .catch(error => console.error('시간 목록 불러오기 실패:', error));
        }

        function loadThemes() {
            fetch('/themes')
                .then(response => response.json())
                .then(themes => {
                    const select = document.getElementById('themeId');
                    themes.forEach(theme => {
                        const option = document.createElement('option');
                        option.value = theme.id;
                        option.textContent = theme.name;
                        select.appendChild(option);
                    });
                })
                .catch(error => console.error('테마 목록 불러오기 실패:', error));
        }

        function loadReservations() {
            fetch('/reservations')
                .then(response => response.json())
                .then(reservations => {
                    const container = document.getElementById('reservationsList');

                    if (reservations.length === 0) {
                        container.innerHTML = '<div class="empty-message">예약 내역이 없습니다.</div>';
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
                                            <button class="btn-delete" onclick="deleteReservation(${reservation.id})">취소</button>
                                        </td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                    `;
                })
                .catch(error => {
                    console.error('예약 목록 불러오기 실패:', error);
                    document.getElementById('reservationsList').innerHTML =
                        '<div class="empty-message">예약 목록을 불러오는데 실패했습니다.</div>';
                });
        }

        document.getElementById('reservationForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const reservationData = {
                name: document.getElementById('name').value,
                date: document.getElementById('date').value,
                timeId: parseInt(document.getElementById('timeId').value),
                themeId: parseInt(document.getElementById('themeId').value)
            };

            fetch('/reservations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reservationData)
            })
            .then(response => {
                if (response.ok) {
                    alert('예약이 완료되었습니다!');
                    document.getElementById('reservationForm').reset();
                    loadReservations();
                } else {
                    return response.json().then(error => {
                        alert('예약 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('예약 생성 실패:', error);
                alert('예약 중 오류가 발생했습니다.');
            });
        });

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
                    loadReservations();
                } else {
                    alert('예약 취소에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('예약 취소 실패:', error);
                alert('예약 취소 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>
