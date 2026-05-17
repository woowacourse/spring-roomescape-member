# 학습 트리

> 구조적 사고를 위한 흐름 지도. 가지가 짧은 부분이 다음 사이클 타겟이다.

```
흐름
│
├── 요청 처리 흐름 (Spring MVC)
│   ├── DispatcherServlet → HandlerMapping → HandlerAdapter
│   ├── @RestController / @ResponseBody
│   ├── ReturnValueHandler → HttpMessageConverter
│   └── GlobalExceptionHandler
│
├── 데이터 처리 흐름 (JPA / JDBC)
│   ├── 영속성 컨텍스트
│   ├── 레이지 로딩 → LazyInitializationException
│   ├── OSIV (Open Session In View)
│   └── 낙관적 락 JDBC 직접 구현 (version 컬럼, WHERE version = ?)
│
├── 트랜잭션 흐름
│   ├── @Transactional (프록시 기반)
│   ├── ThreadLocal → TransactionSynchronizationManager → 커넥션 바인딩
│   ├── 셀프 인보케이션 문제
│   ├── @Async와 트랜잭션 전파
│   ├── readOnly = true 실제 효과 (락 생략, Undo Log 생략, Dirty Checking 생략)
│   ├── [ ] checked vs unchecked 예외와 롤백
│   └── [ ] 트랜잭션 전파와 중첩 트랜잭션 (REQUIRES_NEW)
│
└── 동시성 흐름
    ├── INSERT 경쟁 조건
    ├── Unique Constraint / deleted_at 방식
    ├── 격리 수준 (Read Uncommitted → Serializable)
    │   ├── Dirty Read
    │   ├── Non-Repeatable Read
    │   └── Phantom Read
    ├── MVCC (Multi-Version Concurrency Control)
    ├── 낙관적 락 vs 비관적 락
    └── [ ] 분산 락 (Redis)
```

> `[ ]` 표시는 아직 탐구하지 않은 가지 — 다음 사이클 타겟

---

## 학습 로그

> 각 사이클의 시작 의문을 중심으로 묶었다. 새 주제를 시작하기 전 관련 묶음을 먼저 읽는다.

---

### Spring MVC 흐름

| # | 의문 | 주제 | 링크 |
|---|---|---|---|
| 01 | @RestController는 정확히 어떤 역할인가? | Spring MVC — @RestController, @ResponseBody | [log_01.md](log_01.md) |
| 02 | 컨트롤러 테스트는 어떻게 작성해야 하는가? | Controller 테스트 (MockMvc vs RestAssuredMockMvc) | [log_02.md](log_02.md) |

---

### 동시성 흐름

| # | 의문 | 주제 | 링크 |
|---|---|---|---|
| 03 | INSERT할 때도 동시성 이슈가 생길 수 있지 않나? | 동시성 — INSERT 경쟁 조건과 해결 전략 | [log_03.md](log_03.md) |
| 05 | 격리 수준이란 무엇이고 왜 4단계인가? | 트랜잭션 격리 수준 | [log_05.md](log_05.md) |
| 06 | Repeatable Read가 어떻게 동작하는가? (MVCC) | MVCC / 낙관적 락 / 비관적 락 선택 기준 | [log_06.md](log_06.md) |
| 08 | JPA 없이 낙관적 락을 어떻게 구현하는가? | JDBC 낙관적 락 직접 구현 | [log_08.md](log_08.md) |

---

### 트랜잭션 흐름

| # | 의문 | 주제 | 링크 |
|---|---|---|---|
| 04 | 레이지 로딩과 @Transactional은 어떤 관계인가? | JPA 영속성 / @Transactional | [log_04.md](log_04.md) |
| 07 | readOnly = true는 단순히 쓰기만 막는 건가? | @Transactional(readOnly = true) 실제 효과 | [log_07.md](log_07.md) |
| 09 | 커넥션이 쓰레드에 바인딩된다는 게 어떻게 가능한가? | ThreadLocal과 트랜잭션 커넥션 바인딩 | [log_09.md](log_09.md) |
