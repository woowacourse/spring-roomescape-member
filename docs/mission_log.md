# 🧾 미션 중 기록

<img width="475" height="389" alt="image" src="https://github.com/user-attachments/assets/d60f9849-5ae9-4462-a6a1-438e5bafd1ea" />

- [x] 규칙을 적용해서 변경한 코드 1곳 이상
  - `ReservationTime` ➡️ `Time` ➡️ `TimeSlot` 으로 도메인 객체의 이름이 변경되었지만  
    엔드포인트는 `/time` 로 고정됨 
  - time 은 요구사항에서 식별된 리소스이자 
  - 클라이언트(사용자/관리자) 관점에서 직관적인 엔드포인트이기 때문 
 

- [x] 테스트 작성이 어려웠던 코드 1곳 이상
  - ```java
    @Test
    @DisplayName("기간 내 인기 테마를 예약 건수 기반으로 조회한다.")
    void findPopularThemes() {
        Theme savedTheme = jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        insertReservation(savedTheme.id());
        List<Theme> themes = jdbcThemeRepository.findPopularThemes(10L, LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1));
        assertThat(themes).hasSize(1);
    }
    ``` 
  - 조회의 대상은 `테마`지만 그 조건으로 `예약`에 대한 정보가 필요하고,  
  - `예약`의 생성을 위해선 `시간대`에 대한 정보가 필요.
  - 예약 생성은 조회의 조건이기에 테스트 코드에서 표현할 근거가 있지만  
  - 예약 생성의 조건인 시간대에 대해서도 표현해야 하는가?  
    - `@Sql("/test-setup.sql")` 로 시간대 더미 데이터 추가 및 격리


- [ ] 막힌 순간 1회 이상