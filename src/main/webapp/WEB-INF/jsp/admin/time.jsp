<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>예약 시간 관리</title>
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

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
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

        .times-list {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .times-list h2 {
            color: #667eea;
            margin-bottom: 20px;
            font-size: 1.5em;
        }

        .time-item {
            background: #f8f9fa;
            padding: 20px;
            margin-bottom: 15px;
            border-radius: 10px;
            border-left: 4px solid #667eea;
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: transform 0.2s;
        }

        .time-item:hover {
            transform: translateX(5px);
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }

        .time-info {
            flex: 1;
        }

        .time-value {
            font-size: 1.3em;
            font-weight: 700;
            color: #333;
        }

        .btn-delete {
            background: #e74c3c;
            color: white;
            padding: 8px 20px;
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
        <h1>예약 시간 관리</h1>

        <div class="form-section">
            <h2>새 시간 추가</h2>
            <form id="timeForm">
                <div class="form-group">
                    <label for="startAt">시작 시간</label>
                    <input type="time" id="startAt" name="startAt" required>
                </div>
                <button type="submit" class="btn">시간 추가</button>
            </form>
        </div>

        <div class="times-list">
            <h2>시간 목록</h2>
            <div id="timesList">
                <div class="empty-message">로딩 중...</div>
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
                        container.innerHTML = '<div class="empty-message">등록된 시간이 없습니다.</div>';
                        return;
                    }

                    container.innerHTML = times.map(time => `
                        <div class="time-item">
                            <div class="time-info">
                                <div class="time-value">${time.startAt}</div>
                            </div>
                            <button class="btn-delete" onclick="deleteTime(${time.id})">삭제</button>
                        </div>
                    `).join('');
                })
                .catch(error => {
                    console.error('시간 목록 불러오기 실패:', error);
                    document.getElementById('timesList').innerHTML =
                        '<div class="empty-message">시간 목록을 불러오는데 실패했습니다.</div>';
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
                    alert('시간 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('시간 추가 실패:', error);
                alert('시간 추가 중 오류가 발생했습니다.');
            });
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
                    alert('시간 삭제에 실패했습니다.');
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
