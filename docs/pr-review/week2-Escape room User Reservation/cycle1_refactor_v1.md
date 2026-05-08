# Cycle 1 - PR Review v1 - 수정 사항

---

## ✅ 리팩토링 할 것 목록

- [x] **1. `AdminReservationController`의 `getAllReservations` 변수명 `list`로 되어있는거 다른 이름으로 고치기**
- [x] **2. 재할당이 없는 지역 변수에는 final을 사용하도록 수정**
- [x] **3. `AdminThemeController`의 `createTheme()`에 있는 `adminThemeService.save`를 Long이 아닌 long 타입으로 받도록 수정**
- [ ] **4. `ThemeController`의 `getThemesByCondition` 엔드포인트를 `?condition=popular` 방식 대신 `/themes/popular` path 기반으로 변경**
- [ ] **5. `Reservation` 저장 전/후 생성자 분리 + `save()` 수정 + `equals()`/`hashCode()` 재정의**
  - `Reservation(name, date, time, theme)`: 저장 전 도메인 검증용 (id 없음)
  - `Reservation(id, name, date, time, theme)`: DB 조회 후 생성용 (id null 검증 추가)
  - `ReservationDao.save(Reservation)`: 도메인 객체를 받아 저장 → 검증 우회 방지
  - `equals()`/`hashCode()`: id 기반으로 재정의 (id null이면 object identity 사용)
- [ ] **6. DTO 레이어 분리 - Service가 DTO 대신 Domain 객체만 사용하도록 변경하고, DTO는 `controller` 하위 패키지로 이동**
  - Controller: Request DTO → Domain 객체 변환 후 Service 호출
  - Service: Domain 객체만 사용
  - Controller: Service 결과(Domain) → Response DTO 변환
- [x] **7. 미사용 `SearchRequest` 삭제**
- [ ] **8. DTO의 `date`/`time` 필드를 `String` 대신 `LocalDate`/`LocalTime` 타입으로 유지하기**
  - `ReservationResponse` - `date` (LocalDate), `time` (LocalTime)
  - `AdminReservationResponse` - `date` (LocalDate), `time` (LocalTime)
  - `ReservationTimeResponse` - `startAt` (LocalTime)
  - `AvailableReservationTimeResponse` - `startAt` (LocalTime)
- [x] **9. `repository` 패키지명을 `dao`로 변경**
- [x] **10. `ThemeDao.findById()`의 try-catch를 `query()` + `stream().findFirst()`로 대체**
- [ ] **11. 미흡 테스트 추가**
- [x] **12. `AdminThemeService`의 `delete`에 있는 불필요한 타입 캐스팅 제거**
- [ ] **13. 과거 날짜 검증을 `Reservation` 도메인으로 이동 + `createdAt` 필드 추가**
  - `createdAt`은 외부에서 주입받아 `LocalDate.now()` 직접 호출 방지
- [ ] **14. 서비스 메서드에 트랜잭션 추가**
  - `@Transactional`: `ReservationService.save/delete`, `ReservationTimeService.save/delete`, `AdminThemeService.save/delete`
  - `@Transactional(readOnly = true)`: `AdminReservationService.getAllReservations`, `ReservationService.findAllByName`, `ReservationTimeService.findAll`, `ThemeService.getPopularThemes/getAllThemes/getAvailableTimeResponses`
- [ ] **15. `ThemeService.getPopularThemes()`에서 정책(7일)과 기능 분리 - `startDate`, `endDate`를 외부에서 주입받도록 변경**
- [ ] **16. `Long` → `long` 원시 타입으로 통일 (null 가능성 제거)**
  - `ReservationService.delete()`, `ReservationTimeService.delete()`
  - `ReservationDao.delete()`, `existsByTimeId()`, `existsByThemeId()`
  - `ReservationTimeDao.findById()`, `delete()`
  - `ThemeDao.findById()`
  - `ReservationController`, `ReservationTimeController`의 `@PathVariable Long id`
