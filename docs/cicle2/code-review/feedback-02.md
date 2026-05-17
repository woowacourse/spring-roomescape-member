### 1. DuplicateReservationException 상속 오류

> 이것도 Roomscape를 상속해야 `ProblemDetailsAdvice`에 걸릴 것 같아요~

- 의도/기준/생각
    - 핸들링되지 않고 500 을 보내주고 있었네요!
    - RuntimeException 상속한 RoomescapeException 상속하도록 수정

---

### 2, 6. IllegalArgumentException 핸들링 누락

> 일반적인 IllegalArgumentException 들은 전역 예외 처리에서 빠져 있는 것 같은데, 이 부분도 수정해봅시다

- 의도/기준/생각
    - 서비스 단 예외만 추가하며 누락
    - 커스텀 예외로 포장?
    - 그렇게 따지면 모든 예외를 포장? - 클래스 폭발
    - 표준 예외만으로 표현이 어려운 도메인의 특정한 예외 상황을 표현
    - 표준 예외와 도메인 비즈니스 로직 예외의 균형점 찾기

---

### 3. 데이터 처리/계산 위치의 적절성

> 현재 규모에서는 전체 시간 목록과 예약된 시간 id를 각각 조회한 뒤 자바에서 조합하는 방식도  
> 충분히 이해하기 쉽다고 생각하긴 하는데 예약 가능 여부가 DB의 예약 데이터에서 결정되는 값이라면,  
> 이 책임을 자바에서 계산할지 SQL에서 계산할지 비교해보면 좋은 학습이 될 것 같습니다.

- 의도/기준/생각
    - 인기 테마와 동일 한 상황
    - 자바에서?
        - 할일 많아지고 스코프 추가시마다 다 로직 추가해줄거?
    - DB에서?
        - 사실상 한 번의 쿼리로 싹 조회해서 매핑만 해주면 끝
    - 비교는?
        - 도와줘요 제미나이

`JAVA`

```java
public List<AvailableTimeSlot> findAvailableTimes(long themeId, LocalDate date) {
    validateThemeExists(themeId);
    List<TimeSlot> allTimes = timeSlotRepository.findAll();
    List<Long> reservedIds = reservationRepository.findByThemeIdAndDate(themeId, date);
    return mapToAvailableSlots(allTimes, reservedIds);
}

private void validateThemeExists(long themeId) {
    if (!themeRepository.existsById(themeId)) {
        throw new ThemeNotFoundException();
    }
}

private List<AvailableTimeSlot> mapToAvailableSlots(List<TimeSlot> times, List<Long> reserved) {
    return times.stream()
            .map(time -> new AvailableTimeSlot(time, reserved.contains(time.getId())))
            .toList();
}
```

`DB`

```java
public List<AvailableTimeSlot> findAvailableTimes(long themeId, LocalDate date) {
    validateThemeExists(themeId);
    return timeSlotRepository.findAvailableTimeSlots(themeId, date);
}

private void validateThemeExists(long themeId) {
    if (!themeRepository.existsById(themeId)) {
        throw new ThemeNotFoundException();
    }
}

// Repository
public List<AvailableTimeSlot> findAvailableTimeSlots(long themeId, LocalDate date) {
    String sql = """
            SELECT t.id, t.time, r.id IS NOT NULL AS booked
            FROM time_slot t
            LEFT JOIN reservation r ON t.id = r.time_id AND r.theme_id = ? AND r.date = ?
            """;
    return jdbcTemplate.query(sql, availableTimeSlotMapper(), themeId, date);
}
```

기존 코드가 있다보니 코드는 좀 길어보이지만..

---

### 4. 검증의 우선 순위

> `수정 대상이 존재하는 지`와 `수정하려는 값이 유효한 지` 중 어떤 순서로 검증할지 기준을 잡아보면 좋겠습니다.

- 의도/기준/생각
    - 객체 생성을 뒤로 미루려던 생각 + 중복 시간대 만든김에 한번에 싹 집어넣는 편의주의 콤비네이션

---

### 5. 잘못된 호출 수정

> optional 값이 제대로 활용되지 않고 있어요 👀

- 의도/기준/생각
    - 4번 피드백을 사전 방비했을 기회를 이렇게

---

### 6. @ExceptionHandler 매핑 순서

> 만약 지금의 핸들러를 탄다면 다른 도메인 예외 응답과 달리 code가 빠지고 에러 응답 형식이 달라질 것 같아서요.

- 의도/기준/생각
    - 부모 클래스 핸들러가 있는데 별도로 핸들링중
    - 내부 로직은 동일/오히려 누락된 별도 핸들러
    - 위에서 아래로 순차적으로 매핑하는게 아니네?
    - 구체적인 클레스로 탐색하고 그 다음에 부모 클래스 탐색
    - 스프링 내장 예외와 달리 에러 X - 별개 예외로 인식
    - 아예 같은 예외 핸들러를 2개 이상 뒀을떄만 에러 발

---

### 7. 예외의 관리 위치

> `흐름이 왜 실패했는지를 표현하는 객체` - 발생 위치보다 실패의 의미를 기준으로

- 의도/기준/생각
    - DTO - `계층` 관점에서 데이터 전달
    - Exception - `흐름` 관점에서 실패의 의미
    - 시그니처와 마찬가지로 위치도 의미의 표현을 내포, 명확화를 목적
    - 예약을 찾을 수 없다가 서비스에서 터져도 레포에서 터져도 동일한 의미
    - DB 예외를 서비스가 포장해서 던지면 서비스 패키지에? 레포 패키지에?
    - `어떤 실패를` `어떻게 보여줄` 것인지를 기준으로 관리
    - 실패는 계층보단 흐름에 종속된 개념이란 느낌
    - 계층이 수평이라면 흐름은 수

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---

###                               

>

- 의도/기준/생각
  - 

---
