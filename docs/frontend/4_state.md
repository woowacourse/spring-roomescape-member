# 4. 화면 상태 정의 (Vanilla JS 기준)

프레임워크(React) 없이 Vanilla JS로 상태를 관리할 때는, 데이터를 JS 변수(Model)로 들고 있으면서 데이터가 바뀔 때마다 DOM(View)을 업데이트해주는 방식으로 설계합니다.

## 1. 클라이언트 상태 (Client State)
UI의 조작이나 사용자 폼 입력 등, 프론트엔드 내에서만 유지되는 상태입니다.

### 사용자 메인 화면 (예약)
- `selectedThemeId`: 사용자가 클릭한 테마의 ID (초기값: `null`)
- `selectedDate`: 사용자가 달력에서 선택한 날짜 (초기값: `null`)
- `reservationName`: 예약자 이름 입력 폼의 값 (초기값: `""`)

### 내 예약 조회 화면
- `searchName`: 검색 창에 입력한 내 이름 (초기값: `""`)
- `editTargetReservationId`: 일정 변경 모달이 열렸을 때 대상이 되는 예약의 ID (초기값: `null`)

---

## 2. 서버 상태 (Server State)
API를 통해 백엔드(Spring)에서 가져와 클라이언트가 렌더링을 위해 임시로 보관하는 상태입니다. 항상 로딩, 에러, 성공 상태를 고려해야 합니다.

### 예약 가능한 테마 목록
- **상태 분기**: `Loading` (불러오는 중) ➡️ `Success` (성공) / `Error` (서버 장애)
- **데이터**: `Theme[]` (id, name, description, image_path)

### 예약 가능한 시간 목록
메인 화면에서 날짜와 테마가 모두 선택(`selectedThemeId`, `selectedDate`가 존재)되었을 때만 가져오는 상태입니다.
- **상태 분기**: `Loading` ➡️ `Success` / `Empty` (선택한 날짜에 남은 시간이 없음) / `Error`
- **데이터**: `AvailableTime[]` (id, startAt, isAvailable)

### 내 예약 목록
이름을 검색했을 때 가져오는 상태입니다.
- **상태 분기**: `Loading` ➡️ `Success` / `Empty` (해당 이름으로 된 예약이 없음) / `Error`
- **데이터**: `Reservation[]` (id, themeName, date, time)

### 관리자 데이터 (테마 전체, 시간 전체, 예약 전체)
- **상태 분기**: `Loading` ➡️ `Success` / `Error`
- 관리자 화면 진입 시 각각의 API를 호출하여 테이블이나 리스트 형태로 렌더링합니다.
