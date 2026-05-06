<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>방탈출 예약 시스템</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .container {
            max-width: 1000px;
            width: 100%;
        }

        h1 {
            color: white;
            text-align: center;
            margin-bottom: 50px;
            font-size: 3em;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
        }

        .sections {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
            gap: 30px;
            margin-bottom: 30px;
        }

        .section {
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .section h2 {
            color: #667eea;
            margin-bottom: 25px;
            font-size: 1.8em;
            text-align: center;
            padding-bottom: 15px;
            border-bottom: 3px solid #667eea;
        }

        .menu-list {
            list-style: none;
        }

        .menu-item {
            margin-bottom: 15px;
        }

        .menu-link {
            display: block;
            padding: 18px 25px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 10px;
            text-align: center;
            font-size: 1.1em;
            font-weight: 600;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .menu-link:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .menu-link:active {
            transform: translateY(0);
        }

        .admin .menu-link {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        }

        .admin .menu-link:hover {
            box-shadow: 0 5px 20px rgba(231, 76, 60, 0.4);
        }

        .admin h2 {
            color: #e74c3c;
            border-bottom-color: #e74c3c;
        }

        .footer {
            text-align: center;
            color: white;
            margin-top: 40px;
            font-size: 0.9em;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
        }

        @media (max-width: 768px) {
            .sections {
                grid-template-columns: 1fr;
            }

            h1 {
                font-size: 2em;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔐 방탈출 예약 시스템</h1>

        <div class="sections">
            <div class="section user">
                <h2>👤 사용자 메뉴</h2>
                <ul class="menu-list">
                    <li class="menu-item">
                        <a href="/theme" class="menu-link">🎭 테마 둘러보기</a>
                    </li>
                    <li class="menu-item">
                        <a href="/reservation" class="menu-link">📅 예약하기</a>
                    </li>
                </ul>
            </div>

            <div class="section admin">
                <h2>👨‍💼 관리자 메뉴</h2>
                <ul class="menu-list">
                    <li class="menu-item">
                        <a href="/admin/theme" class="menu-link">🎨 테마 관리</a>
                    </li>
                    <li class="menu-item">
                        <a href="/admin/reservation" class="menu-link">📋 예약 관리</a>
                    </li>
                    <li class="menu-item">
                        <a href="/admin/time" class="menu-link">⏰ 시간 관리</a>
                    </li>
                </ul>
            </div>
        </div>

        <div class="footer">
            <p>방탈출 예약 시스템 v1.0</p>
        </div>
    </div>
</body>
</html>
