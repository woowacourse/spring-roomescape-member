<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>예약 관리</title>
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
            max-width: 1400px;
            margin: 0 auto;
        }

        h1 {
            color: white;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
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
        <h1>예약 관리</h1>

        <div class="reservations-list">
            <h2>전체 예약 목록</h2>
            <div id="reservationsList">
                <div class="empty-message">로딩 중...</div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            loadReservations();
        });

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
                                    <th>ID</th>
                                    <th>이름</th>
                                    <th>날짜</th>
                                    <th>시간</th>
                                    <th>테마</th>
                                    <th>생성일시</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${reservations.map(reservation => `
                                    <tr>
                                        <td>${reservation.id}</td>
                                        <td>${reservation.name}</td>
                                        <td>${reservation.date}</td>
                                        <td>${reservation.time.startAt}</td>
                                        <td>${reservation.theme.name}</td>
                                        <td>${new Date(reservation.createdAt).toLocaleString('ko-KR')}</td>
                                        <td>
                                            <button class="btn-delete" onclick="deleteReservation(${reservation.id})">삭제</button>
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

        function deleteReservation(id) {
            if (!confirm('정말로 이 예약을 삭제하시겠습니까?')) {
                return;
            }

            fetch(`/reservations/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('예약이 삭제되었습니다.');
                    loadReservations();
                } else {
                    alert('예약 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('예약 삭제 실패:', error);
                alert('예약 삭제 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>
