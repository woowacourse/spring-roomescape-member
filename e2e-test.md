# Playwright E2E 테스트 가이드

이 문서는 `spring-roomescape-member` 프로젝트에 Playwright 기반 E2E 테스트를 설치하고 실행하는 방법을 정리합니다.

## 1. 준비 사항

- Node.js 20+ 권장
- npm 사용 가능 환경
- Java 21

현재 E2E 테스트는 Playwright가 Spring Boot 서버를 자동으로 띄운 뒤 `http://127.0.0.1:8080` 기준으로 검증합니다.

## 2. 최초 설치

프로젝트 루트에서 아래 명령을 실행합니다.

```bash
npm install
npx playwright install chromium
```

설치가 끝나면 아래 파일/디렉터리를 기준으로 동작합니다.

- `package.json`: E2E 실행 스크립트
- `playwright.config.js`: Playwright 설정과 Spring Boot 자동 실행 설정
- `e2e/roomescape.spec.js`: E2E 시나리오

## 3. 실행 방법

기본 실행:

```bash
npm run e2e
```

브라우저를 보면서 실행:

```bash
npm run e2e:headed
```

Playwright UI 모드:

```bash
npm run e2e:ui
```

이미 서버를 직접 띄워 둔 상태에서 실행:

```bash
GRADLE_USER_HOME=.gradle-playwright ./gradlew bootRun
```

다른 터미널에서:

```bash
npm run e2e:external-server
```

마지막 HTML 리포트 확인:

```bash
npm run e2e:report
```

## 4. 포함된 시나리오

현재 작성된 E2E 테스트는 아래 흐름을 검증합니다.

1. 사용자가 예약 페이지에서 예약을 생성하면 관리자 예약 페이지에서 해당 예약을 확인할 수 있다.
2. 관리자가 테마를 추가하면 사용자 예약 페이지의 전체 테마 목록에서 새 테마가 조회된다.
3. 관리자가 테마를 삭제하면 사용자 예약 페이지의 전체 테마 목록에서 해당 테마가 사라진다.
4. 예약된 시간은 사용자 예약 화면에서 비활성화되고, 관리자가 취소하면 다시 예약 가능해진다.

## 5. 테스트 동작 방식

- Playwright의 `webServer` 설정이 `./gradlew bootRun`을 실행합니다.
- Gradle 캐시는 워크스페이스 내부의 `.gradle-playwright/`를 사용하도록 설정되어 있어, 실행 환경별 홈 디렉터리 권한 문제를 줄입니다.
- 필요하면 `PW_SKIP_WEBSERVER=1`로 `webServer` 기동을 건너뛰고, 이미 실행 중인 로컬 서버를 재사용할 수 있습니다.
- 애플리케이션은 인메모리 H2 DB를 사용하므로 서버를 새로 띄우면 초기 데이터가 다시 적재됩니다.
- 테스트 데이터 충돌을 피하기 위해 E2E 테스트는 실행 시점의 타임스탬프를 이름에 붙여 고유 값을 사용합니다.
- 예약 가능한 슬롯 조회는 UI 조작 전에 `/api/themes/{id}?date=...` API를 호출해 현재 예약 가능한 시간 하나를 찾은 뒤 진행합니다.

## 6. 문제 해결

브라우저가 설치되지 않아 실패하면:

```bash
npx playwright install chromium
```

`8080` 포트가 이미 사용 중이면 기존 프로세스를 종료한 뒤 다시 실행합니다.

서버 기동이 오래 걸리면 `playwright.config.js`의 `webServer.timeout` 값을 늘리면 됩니다.

실패 원인을 더 자세히 보려면 Playwright가 남긴 아래 산출물을 확인하면 됩니다.

- `playwright-report/`
- `test-results/`
