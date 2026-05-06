<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>방탈출 테마</title>
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
            margin-bottom: 20px;
            font-size: 2.5em;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
        }

        .section-title {
            color: white;
            font-size: 1.8em;
            margin: 40px 0 20px;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
            border-bottom: 3px solid rgba(255, 255, 255, 0.3);
            padding-bottom: 10px;
        }

        .section-title:first-of-type {
            margin-top: 20px;
        }

        .themes-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }

        .theme-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
        }

        .theme-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
        }

        .theme-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.2em;
        }

        .theme-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .theme-content {
            padding: 20px;
        }

        .theme-name {
            font-size: 1.4em;
            font-weight: 700;
            color: #333;
            margin-bottom: 10px;
        }

        .theme-description {
            color: #666;
            line-height: 1.6;
        }

        .empty-message {
            background: white;
            text-align: center;
            color: #999;
            padding: 60px 40px;
            border-radius: 15px;
            font-size: 1.2em;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .popular-badge {
            position: relative;
        }

        .popular-badge::after {
            content: '🔥 인기';
            position: absolute;
            top: 15px;
            right: 15px;
            background: #e74c3c;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9em;
            font-weight: 600;
            box-shadow: 0 2px 10px rgba(231, 76, 60, 0.4);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>방탈출 테마</h1>

        <h2 class="section-title">인기 테마</h2>
        <div id="popularThemes" class="themes-grid">
            <div class="empty-message">로딩 중...</div>
        </div>

        <h2 class="section-title">전체 테마</h2>
        <div id="allThemes" class="themes-grid">
            <div class="empty-message">로딩 중...</div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            loadPopularThemes();
            loadAllThemes();
        });

        function loadPopularThemes() {
            fetch('/themes/popular')
                .then(response => response.json())
                .then(themes => {
                    const container = document.getElementById('popularThemes');

                    if (themes.length === 0) {
                        container.innerHTML = '<div class="empty-message">인기 테마가 없습니다.</div>';
                        return;
                    }

                    container.innerHTML = themes.map(theme => createThemeCard(theme, true)).join('');
                })
                .catch(error => {
                    console.error('인기 테마 불러오기 실패:', error);
                    document.getElementById('popularThemes').innerHTML =
                        '<div class="empty-message">인기 테마를 불러오는데 실패했습니다.</div>';
                });
        }

        function loadAllThemes() {
            fetch('/themes')
                .then(response => response.json())
                .then(themes => {
                    const container = document.getElementById('allThemes');

                    if (themes.length === 0) {
                        container.innerHTML = '<div class="empty-message">등록된 테마가 없습니다.</div>';
                        return;
                    }

                    container.innerHTML = themes.map(theme => createThemeCard(theme, false)).join('');
                })
                .catch(error => {
                    console.error('테마 목록 불러오기 실패:', error);
                    document.getElementById('allThemes').innerHTML =
                        '<div class="empty-message">테마 목록을 불러오는데 실패했습니다.</div>';
                });
        }

        function createThemeCard(theme, isPopular) {
            return `
                <div class="theme-card ${isPopular ? 'popular-badge' : ''}">
                    <div class="theme-image">
                        <img src="${theme.url}" alt="${theme.name}"
                             onerror="this.parentElement.innerHTML='<div>${theme.name}</div>'">
                    </div>
                    <div class="theme-content">
                        <div class="theme-name">${theme.name}</div>
                        <div class="theme-description">${theme.description}</div>
                    </div>
                </div>
            `;
        }
    </script>
</body>
</html>
