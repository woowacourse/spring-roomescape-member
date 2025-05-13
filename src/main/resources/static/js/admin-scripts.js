document.addEventListener('DOMContentLoaded', function () {
    updateUIBasedOnLogin();
});

document.getElementById('logout-btn').addEventListener('click', function (event) {
    event.preventDefault();
    fetch('/admins/logout', {
        method: 'POST', // 또는 서버 설정에 따라 GET 일 수도 있음
        credentials: 'include' // 쿠키를 포함시키기 위해 필요
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공, 페이지 새로고침 또는 리다이렉트
                localStorage.removeItem('role'); // 로컬 스토리지에서 역할 제거
                window.location.href = '/';
            } else {
                // 로그아웃 실패 처리
                console.error('Logout failed');
                alert('Logout failed');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message)
        });
});

function updateUIBasedOnLogin() {
    if (localStorage.getItem("role") !== "admin") {
        return;
    }
    fetch('/admins/my/name')
        .then(response => {
            if (!response.ok) {
                throw new Error('Not logged in or other error');
            }
            return response.json();
        })
        .then(data => {
            // 응답에서 사용자 이름을 추출하여 UI 업데이트
            document.getElementById('profile-name').textContent = data.name;
            document.querySelector('.nav-item.dropdown').style.display = 'block';
            document.querySelector('.nav-item a[href="/admin/login"]').parentElement.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('profile-name').textContent = 'Profile';
            document.querySelector('.nav-item.dropdown').style.display = 'none';
            document.querySelector('.nav-item a[href="/admin/login"]').parentElement.style.display = 'block';
        });
}

// 드롭다운 메뉴 토글
document.getElementById("navbarDropdown").addEventListener('click', function (e) {
    e.preventDefault();
    const dropdownMenu = e.target.closest('.nav-item.dropdown').querySelector('.dropdown-menu');
    dropdownMenu.classList.toggle('show'); // Bootstrap 4에서는 data-toggle 사용, Bootstrap 5에서는 JS로 처리
});


function login() {
    const loginId = document.getElementById('login-id').value;
    const password = document.getElementById('password').value;

    // 입력 필드 검증
    if (!loginId || !password) {
        alert('Please fill in all fields.');
        return; // 필수 입력 필드가 비어있으면 여기서 함수 실행을 중단
    }

    fetch('/admins/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            loginId: loginId,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                alert('Login failed'); // 로그인 실패 시 경고창 표시
                throw new Error('Login failed');
            }
        })
        .then(() => {
            localStorage.setItem('role', 'admin'); // 로그인 성공 시 로컬 스토리지에 역할 저장
            updateUIBasedOnLogin(); // UI 업데이트
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Error during login:', error);
            alert(error.message);
        });
}

function signup() {
    // Redirect to signup page
    window.location.href = '/signup';
}

function register(event) {
    // 폼 데이터 수집
    const loginId = document.getElementById('loginId').value;
    const password = document.getElementById('password').value;
    const name = document.getElementById('name').value;

    // 입력 필드 검증
    if (!loginId || !password || !name) {
        alert('Please fill in all fields.');
        return; // 필수 입력 필드가 비어있으면 여기서 함수 실행을 중단
    }

    // 요청 데이터 포맷팅
    const formData = {
        loginId: loginId,
        password: password,
        name: name
    };

    // AJAX 요청 생성 및 전송
    fetch('/admins', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                alert('Signup request failed');
                throw new Error('Signup request failed');
            }
            return response.json(); // 여기서 응답을 JSON 형태로 변환
        })
        .then(data => {
            // 성공적인 응답 처리
            console.log('Signup successful:', data);
            window.location.href = '/admin/login';
        })
        .catch(error => {
            // 에러 처리
            console.error('Error during signup:', error);
            alert(error.message)
        });

    // 폼 제출에 의한 페이지 리로드 방지
    event.preventDefault();
}

function base64DecodeUnicode(str) {
    // Base64 디코딩
    const decodedBytes = atob(str);
    // UTF-8 바이트를 문자열로 변환
    const encodedUriComponent = decodedBytes.split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('');
    return decodeURIComponent(encodedUriComponent);
}
