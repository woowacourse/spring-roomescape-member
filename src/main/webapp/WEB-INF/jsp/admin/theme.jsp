<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>방탈출 테마 관리</title>
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

        .btn:active {
            transform: translateY(0);
        }

        .themes-list {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .themes-list h2 {
            color: #667eea;
            margin-bottom: 20px;
            font-size: 1.5em;
        }

        .theme-item {
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

        .theme-item:hover {
            transform: translateX(5px);
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }

        .theme-info {
            flex: 1;
        }

        .theme-name {
            font-size: 1.2em;
            font-weight: 700;
            color: #333;
            margin-bottom: 8px;
        }

        .theme-description {
            color: #666;
            margin-bottom: 5px;
        }

        .theme-url {
            color: #667eea;
            font-size: 0.9em;
            text-decoration: none;
        }

        .theme-url:hover {
            text-decoration: underline;
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
        <h1>방탈출 테마 관리</h1>

        <div class="form-section">
            <h2>새 테마 추가</h2>
            <form id="themeForm">
                <div class="form-group">
                    <label for="name">테마 이름</label>
                    <input type="text" id="name" name="name" required placeholder="예: 범인은 바로 너!">
                </div>
                <div class="form-group">
                    <label for="description">설명</label>
                    <input type="text" id="description" name="description" required placeholder="테마 설명을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="url">썸네일 URL</label>
                    <input type="text" id="url" name="url" required placeholder="https://example.com/image.jpg">
                </div>
                <button type="submit" class="btn">테마 추가</button>
            </form>
        </div>

        <div class="themes-list">
            <h2>테마 목록</h2>
            <div id="themesList">
                <div class="empty-message">로딩 중...</div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            loadThemes();
        });

        function loadThemes() {
            fetch('/themes')
                .then(response => response.json())
                .then(themes => {
                    const themesList = document.getElementById('themesList');

                    if (themes.length === 0) {
                        themesList.innerHTML = '<div class="empty-message">등록된 테마가 없습니다.</div>';
                        return;
                    }

                    themesList.innerHTML = themes.map(theme => `
                        <div class="theme-item">
                            <div class="theme-info">
                                <div class="theme-name">${theme.name}</div>
                                <div class="theme-description">${theme.description}</div>
                                <a href="${theme.url}" target="_blank" class="theme-url">${theme.url}</a>
                            </div>
                            <button class="btn-delete" onclick="deleteTheme(${theme.id})">삭제</button>
                        </div>
                    `).join('');
                })
                .catch(error => {
                    console.error('테마 목록 불러오기 실패:', error);
                    document.getElementById('themesList').innerHTML =
                        '<div class="empty-message">테마 목록을 불러오는데 실패했습니다.</div>';
                });
        }

        document.getElementById('themeForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const themeData = {
                name: document.getElementById('name').value,
                description: document.getElementById('description').value,
                url: document.getElementById('url').value
            };

            fetch('/admin/themes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(themeData)
            })
            .then(response => {
                if (response.ok) {
                    alert('테마가 추가되었습니다!');
                    document.getElementById('themeForm').reset();
                    loadThemes();
                } else {
                    alert('테마 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('테마 추가 실패:', error);
                alert('테마 추가 중 오류가 발생했습니다.');
            });
        });

        function deleteTheme(id) {
            if (!confirm('정말로 이 테마를 삭제하시겠습니까?')) {
                return;
            }

            fetch(`/admin/themes/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('테마가 삭제되었습니다.');
                    loadThemes();
                } else {
                    alert('테마 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('테마 삭제 실패:', error);
                alert('테마 삭제 중 오류가 발생했습니다.');
            });
        }
    </script>
</body>
</html>