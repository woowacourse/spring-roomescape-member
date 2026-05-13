# 방탈출 예약 관리

방탈출 예약, 예약 시간, 테마를 관리하는 Spring Boot 미션입니다.

## 프로젝트 구조

```text
.
├── BE   # Spring Boot 백엔드 (REST API 전용)
└── FE   # 사용자/관리자 화면 (Vanilla JS, 빌드 도구 없음)
```

## 실행 방법

### 사전 요구 사항

- JDK 21
- Node.js 18+ (FE 서버 실행용)

### 백엔드 실행

```bash
cd BE
./gradlew bootRun
```

### 프론트엔드 실행

```bash
cd FE
node dev-server.mjs
```

실행 후 브라우저에서 아래 주소로 접속할 수 있습니다:

- **사용자 페이지**: [http://localhost:3000/user/index.html](http://localhost:3000/user/index.html)
- **관리자 페이지**: [http://localhost:3000/admin/index.html](http://localhost:3000/admin/index.html)

### 테스트

```bash
cd BE
./gradlew test
```

## 기능 목록

### 예약 시간 관리

- 예약 시간 생성
- 예약 시간 목록 조회
- 예약 시간 삭제
- 예약에서 참조 중인 예약 시간은 삭제할 수 없도록 검증

### 테마 관리

- 테마 생성
- 테마 목록 조회
- 테마 삭제
- 테마 생성 시 이름, 설명, 썸네일 URL 입력

### 예약 관리

- 예약 생성
- 예약 목록 조회
- 예약 목록 이름 기반 조회
- 예약 삭제(관리자)
- 예약 삭제(본인)
- 예약 수정(날짜 & 시간)
- 예약 가능한 시간 목록 조회
- 예약 생성 시 예약자 이름, 예약 날짜, 예약 시간 ID, 테마 ID 입력
- 예약 시간 ID와 테마 ID를 통해 예약을 연결
- 존재하지 않는 예약 시간 ID나 테마 ID로 예약 생성 시 예외 발생

## 예외 처리 및 상세 API 명세

본 프로젝트의 상세한 오류 처리 정책과 API 명세는 아래 문서에서 확인할 수 있습니다.

- [상세 API 명세서 (docs/API.md)](docs/API.md)
- [오류 처리 가이드 (docs/ERROR_HANDLING.md)](docs/ERROR_HANDLING.md)
