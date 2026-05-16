# 6. 컴포넌트 설계 (Vanilla JS)

React를 사용하지 않더라도 유지보수를 위해 화면(UI)을 의미 있는 단위로 쪼개어 JS 클래스나 함수로 만듭니다. 상위 모듈이 하위 모듈에게 데이터를 파라미터(React의 Props 역할)로 넘겨주며 화면을 조립합니다.

## 1. 예약하기 화면 (Main Page)

### 1.1. `ThemeList` 컴포넌트
- **책임**: 서버에서 받아온 테마 목록을 화면에 렌더링하고, 테마 클릭 이벤트를 처리한다.
- **전달받는 값(Props 역할)**: `themes` (테마 배열), `onSelectTheme` (클릭 시 실행할 콜백 함수)

### 1.2. `Calendar` 컴포넌트
- **책임**: 달력 UI를 그리고, 사용자가 날짜를 클릭하면 이벤트를 발생시킨다.
- **전달받는 값(Props 역할)**: `onSelectDate` (클릭 시 실행할 콜백 함수)

### 1.3. `TimeSlotList` 컴포넌트
- **책임**: 테마와 날짜가 선택되었을 때, 예약 가능한 시간 버튼들을 렌더링한다.
- **전달받는 값(Props 역할)**: `times` (시간 배열), `onSelectTime` (클릭 시 실행할 콜백 함수)

## 2. 내 예약 조회 화면 (My Reservations Page)

### 2.1. `ReservationSearchForm` 컴포넌트
- **책임**: 이름 입력 폼을 그리고 검색 이벤트를 처리한다.
- **전달받는 값(Props 역할)**: `onSubmit` (조회 버튼 클릭 시 실행할 콜백 함수)

### 2.2. `ReservationList` 컴포넌트
- **책임**: 조회된 예약 데이터 배열을 카드 형태로 순회하며 그린다.
- **전달받는 값(Props 역할)**: `reservations` (예약 데이터 배열), `onClickEdit` (변경 클릭 콜백), `onClickCancel` (취소 클릭 콜백)

### 2.3. `ScheduleEditModal` 컴포넌트 (공통 UI)
- **책임**: 예약을 수정하는 모달 창을 띄우고, 새로운 날짜/시간을 입력받는다.
- **전달받는 값(Props 역할)**: `reservation` (기존 예약 정보), `onSave` (저장 콜백), `onClose` (닫기 콜백)
