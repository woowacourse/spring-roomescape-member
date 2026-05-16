# 7. 로직 분리 (Vanilla JS Custom Hook 대체)

Vanilla JS에서는 React의 Custom Hook(`use-`로 시작하는 함수)을 사용할 수 없으므로, UI 컴포넌트에서 순수 비즈니스 로직을 별도의 JS 함수나 모듈 파일로 분리하여 설계합니다.

## 1. API 통신 모듈 (`api.js`)
UI 컴포넌트 내부에 `fetch`를 직접 쓰지 않고, 순수 데이터 요청만 담당하는 함수로 뺍니다.
- `fetchThemes()`: 전체 테마 목록 조회
- `fetchAvailableTimes(themeId, date)`: 예약 가능 시간 조회
- `createReservation(data)`: 예약 생성 POST 요청
- `cancelReservation(id)`: 예약 취소 DELETE/PUT 요청

## 2. 폼 유효성 검사 모듈 (`validator.js`)
사용자의 입력값을 검증하는 순수 함수 모음입니다. UI 로직과 섞이지 않도록 분리합니다.
- `validateName(name)`: 이름이 빈 값인지, 길이 제한을 넘지 않는지 검사하여 에러 메시지 반환.
- `validateReservationForm(data)`: 테마, 날짜, 시간이 모두 선택되었는지 통합 검사.

## 3. 공통 UI 유틸 모듈 (`utils.js`)
여러 컴포넌트에서 반복되는 DOM 조작 로직을 함수로 분리합니다. (React의 커스텀 훅 역할을 가장 많이 대신함)
- `showToast(message, type)`: 화면 하단에 토스트 알림을 렌더링하고 `setTimeout`을 통해 3초 뒤에 DOM에서 삭제하는 로직.
- `toggleLoading(buttonElement, isLoading)`: 버튼 안에 로딩 스피너를 넣고 `disabled` 속성을 토글하는 로직.

## 4. 상태 관리 객체 (`store.js` 또는 옵저버 패턴)
단순한 변수로는 관리가 복잡해질 때, 전역적으로 상태를 관리하는 객체를 만듭니다.
- `ReservationStore`: 사용자가 선택한 `themeId`, `date`, `timeId`를 들고 있으며, 상태가 바뀔 때마다 이를 구독(Subscribe)하고 있는 UI 컴포넌트들의 `render` 함수를 다시 호출해줍니다.
