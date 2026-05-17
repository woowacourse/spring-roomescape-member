<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>시간 관리 - 방탈출 예약</title>
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

        .form-card {
            background: #151932;
            border: 1px solid #1f2547;
            border-radius: 12px;
            padding: 32px;
            margin-bottom: 40px;
        }

        .form-card h2 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 24px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #c5cae9;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .form-group input[type="time"] {
            width: 100%;
            padding: 12px 16px;
            background: #0a0e27;
            border: 1px solid #1f2547;
            border-radius: 8px;
            font-size: 1.1rem;
            color: #ffffff;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .form-group input[type="time"]:focus {
            outline: none;
            border-color: #667eea;
            background: #0d1129;
        }

        .form-group input[type="time"]::-webkit-calendar-picker-indicator {
            filter: invert(1);
            cursor: pointer;
        }

        .btn {
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

        .time-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 16px;
        }

        .time-item {
            background: #1a1f3a;
            padding: 20px;
            border-radius: 10px;
            border: 1px solid #1f2547;
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: all 0.2s ease;
        }

        .time-item:hover {
            border-color: #2d3561;
        }

        .time-value {
            font-size: 1.2rem;
            font-weight: 600;
            color: #ffffff;
        }

        .btn-delete {
            background: #f5576c;
            color: white;
            padding: 6px 12px;
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

        .btn-edit {
            background: #667eea;
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            font-size: 0.85rem;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
            margin-right: 6px;
        }

        .btn-edit:hover {
            background: #5a6fd6;
        }

        .modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.6);
            z-index: 100;
            justify-content: center;
            align-items: center;
        }

        .modal-overlay.active {
            display: flex;
        }

        .modal {
            background: #151932;
            border: 1px solid #1f2547;
            border-radius: 12px;
            padding: 32px;
            width: 100%;
            max-width: 400px;
        }

        .modal h2 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 24px;
        }

        .modal-actions {
            display: flex;
            gap: 12px;
            justify-content: flex-end;
            margin-top: 24px;
        }

        .btn-cancel {
            background: #2d3561;
            color: #c5cae9;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 0.95rem;
            font-weight: 600;
            font-family: 'Noto Sans KR', sans-serif;
            transition: all 0.2s ease;
        }

        .btn-cancel:hover {
            background: #3a4575;
        }

        .empty-state {
            text-align: center;
            color: #5c6686;
            padding: 40px;
            font-size: 0.95rem;
        }

        @media (max-width: 768px) {
            h1 {
                font-size: 2rem;
            }

            .time-grid {
                grid-template-columns: 1fr;
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
            <h1>시간 관리</h1>
        </header>

        <div class="form-card">
            <h2>새 시간 추가</h2>
            <form id="timeForm">
                <div class="form-group">
                    <label for="startAt">시작 시간</label>
                    <input type="time" id="startAt" name="startAt" required>
                </div>
                <button type="submit" class="btn">시간 추가</button>
            </form>
        </div>

        <div class="list-card">
            <h2>등록된 시간 목록</h2>
            <div id="timesList">
                <div class="empty-state">로딩 중...</div>
            </div>
        </div>
    </div>

    <div class="modal-overlay" id="editModal">
        <div class="modal">
            <h2>시간 수정</h2>
            <div class="form-group">
                <label for="editStartAt">시작 시간</label>
                <input type="time" id="editStartAt" name="editStartAt" required>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="closeEditModal()">취소</button>
                <button class="btn" onclick="submitEditTime()">수정</button>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            loadTimes();
        });

        function loadTimes() {
            fetch('/times')
                .then(response => response.json())
                .then(times => {
                    const container = document.getElementById('timesList');

                    if (times.length === 0) {
                        container.innerHTML = '<div class="empty-state">등록된 시간이 없습니다.</div>';
                        return;
                    }

                    container.innerHTML = '<div class="time-grid">' + times.map(time => `
                        <div class="time-item">
                            <div class="time-value">${time.startAt}</div>
                            <div>
                                <button class="btn-edit" onclick="openEditModal(${time.id}, '${time.startAt}')">수정</button>
                                <button class="btn-delete" onclick="deleteTime(${time.id})">삭제</button>
                            </div>
                        </div>
                    `).join('') + '</div>';
                })
                .catch(error => {
                    console.error('시간 목록 불러오기 실패:', error);
                    document.getElementById('timesList').innerHTML =
                        '<div class="empty-state">시간 목록을 불러오는데 실패했습니다.</div>';
                });
        }

        document.getElementById('timeForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const timeData = {
                startAt: document.getElementById('startAt').value
            };

            fetch('/admin/times', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(timeData)
            })
            .then(response => {
                if (response.ok) {
                    alert('시간이 추가되었습니다!');
                    document.getElementById('timeForm').reset();
                    loadTimes();
                } else {
                    return response.json().then(error => {
                        alert('시간 추가 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('시간 추가 실패:', error);
                alert('시간 추가 중 오류가 발생했습니다.');
            });
        });

        let editingTimeId = null;

        function openEditModal(id, startAt) {
            editingTimeId = id;
            document.getElementById('editStartAt').value = startAt;
            document.getElementById('editModal').classList.add('active');
        }

        function closeEditModal() {
            editingTimeId = null;
            document.getElementById('editModal').classList.remove('active');
        }

        function submitEditTime() {
            if (!editingTimeId) return;

            const timeData = {
                startAt: document.getElementById('editStartAt').value
            };

            fetch(`/admin/times/${editingTimeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(timeData)
            })
            .then(response => {
                if (response.ok) {
                    alert('시간이 수정되었습니다.');
                    closeEditModal();
                    loadTimes();
                } else {
                    return response.json().then(error => {
                        alert('시간 수정 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('시간 수정 실패:', error);
                alert('시간 수정 중 오류가 발생했습니다.');
            });
        }

        document.getElementById('editModal').addEventListener('click', function(e) {
            if (e.target === this) closeEditModal();
        });

        function deleteTime(id) {
            if (!confirm('정말로 이 시간을 삭제하시겠습니까?')) {
                return;
            }

            fetch(`/admin/times/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('시간이 삭제되었습니다.');
                    loadTimes();
                } else {
                    return response.json().then(error => {
                        alert('시간 삭제 실패: ' + (error.message || '알 수 없는 오류'));
                    });
                }
            })
            .catch(error => {
                console.error('시간 삭제 실패:', error);
                alert('시간 삭제 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>
