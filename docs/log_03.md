# 학습 로그 #03

**시간**: 05/14 (약 __ 분)
**학습 범위**: 동시성 — INSERT 경쟁 조건과 해결 전략

## 1. 막힌 것의 종류

이번에 막힌 것은 어떤 종류의 어려움이었는가? (해당하는 것에 체크)

- [ ] 개념 자체를 모르겠다 (예: "스프링 빈이 뭔지 모르겠다")
- [ ] 개념은 알겠는데 코드로 어떻게 쓰는지 모르겠다 (예: "JdbcTemplate 문법을 모르겠다")
- [ ] 코드는 돌아가는데 이게 맞는 건지 모르겠다 (예: "계층 분리를 이렇게 해도 되나?")
- [x] 기타: 토론중에 INSERT 시 동시성 이슈 가능성이 있다는점을 알게되었다. 하지만 왜 문제가 되는지, 어떻게 해결해야 하는지 몰랐다

## 2. 이번 타임의 학습 전략

- 이전에 바꾸기로 한 전략은 무엇이었고, 실행했는가?
  AI와 대화를 통해 키워드를 탐색하고, 각 방법의 한계를 이해하면서 적합한 해결책을 좁혀가는 방식으로 진행했다.

- 실제로 어떻게 학습했는지 디테일한 과정을 써보세요.

### 학습 과정

#### 의문의 시작

> "INSERT할 때에도 동시성 이슈가 생길 수 있지 않나?"

두 요청이 동시에 `existsByThemeIdAndTimeIdAndDate` 체크를 통과하면 중복 예약이 삽입될 수 있다.

```
Thread A: exists 체크 → false
Thread B: exists 체크 → false
Thread A: INSERT → 성공
Thread B: INSERT → 중복 삽입!
```

#### 해결 방법 탐색

**비관적 락 (SELECT FOR UPDATE)**

처음 생각한 방법. 하지만 핵심 한계를 발견했다.

- SELECT FOR UPDATE는 이미 존재하는 행을 잠그는 방식
- 잠글 행이 없으면 락이 걸리지 않음
- → INSERT 경쟁에는 직접 적용 불가

"프록시 락 패턴"(다른 테이블의 기존 행을 대리 자원으로 잠금)으로 우회는 가능하지만, 락의 범위가 너무 넓어지는 트레이드오프가 있음.

**낙관적 락 (@Version)**

`version` 컬럼으로 "읽은 뒤 수정" 충돌을 감지하는 패턴.

- 수정 충돌 감지용이라 새 행 삽입 경쟁에는 해당 없음
- → INSERT 경쟁에는 직접 적용 불가 (비관적 락과 같은 이유)

**Unique Constraint**

DB가 원자적으로 중복을 차단하는 가장 적합한 방법.

- 하지만 Soft delete를 사용 중이라 취소된 행과 충돌 발생
- → 직접 사용 불가

**Partial Index (필터 인덱스)** — 이론상 적합하나 H2에서 오류 발생

`WHERE status <> 'CANCELED'` 조건으로 활성 예약에만 유니크 제약 적용하는 방식.
Soft delete와 Unique Constraint를 동시에 만족하는 이론적으로 가장 깔끔한 해결책.

```sql
CREATE UNIQUE INDEX idx_active_reservation ON reservations (date, time_id, theme_id) WHERE status <> 'CANCELED';
```

→ 실제 적용 시 H2에서 `UNIQUE ... WHERE` 구문 오류 발생. H2가 문서상 지원한다고 알려졌지만 실제로는 동작하지 않았다.

**deleted_at 컬럼** ✅ 최종 채택

취소 시 `deleted_at`에 타임스탬프를 기록하고, `UNIQUE(date, time_id, theme_id, deleted_at)`을 적용.
활성 예약은 `deleted_at = NULL`이므로 NULL은 유니크 인덱스에서 제외되어 중복 허용.
취소된 예약은 각기 다른 타임스탬프를 가지므로 충돌 없음.

```sql
ALTER TABLE reservations ADD COLUMN deleted_at TIMESTAMP NULL;
CREATE UNIQUE INDEX idx_active_reservation ON reservations (date, time_id, theme_id, deleted_at);
```

#### DB별 지원 현황

| DB                    | Partial Index (`WHERE`) | deleted_at 우회책 |
|-----------------------|-------------------------|-------------------|
| PostgreSQL, MS SQL Server | ✅                  | 가능              |
| H2                    | ❌ (실제 동작 안 함)    | ✅ 채택           |
| MySQL, MariaDB        | ❌                      | ✅                |
| SQLite                | ✅                      | 가능              |

#### 핵심 인사이트

- **비관적 락 / 낙관적 락** → UPDATE 경쟁에 적합
- **INSERT 경쟁** → DB 레벨 Unique Constraint가 근본적인 해결책
- Soft delete를 선택하면 유니크 제약 적용이 복잡해지는 트레이드오프가 생김

## 3. 전략 평가

- 효과적이었던 것과 그 이유
  각 방법의 "왜 안 되는지"를 순차적으로 확인하면서 좁혀간 방식이 효과적이었다.
  최종 선택(Partial Index)의 이유가 단순히 "이게 답이야"가 아니라, 나머지를 직접 탈락시킨 결과로 남았기 때문에 기억에 오래 남을 것 같다.

- 비효과적이었던 것과 그 이유
  비관적 락과 낙관적 락이 INSERT 경쟁에 왜 안 되는지 처음에 직관적으로 이해되지 않았다.
  "잠글 행이 없으면 락이 걸리지 않는다"는 핵심을 파악하는 데 시간이 걸렸다.

- 막힌 것의 종류(1번)와 전략의 궁합은 어땠는가?
  실제 코드에서 발생하는 문제를 출발점으로 삼았기 때문에 탐색 전략과 잘 맞았다.
  이론으로 먼저 배우는 것보다 "내 코드에서 왜 문제가 되는가"라는 맥락이 있어서 이해가 빨랐다.

## 4. AI 피드백

비관적 락 → 낙관적 락 → Unique Constraint → Partial Index 순으로 각 방법의 한계를 직접 확인하며 좁혀갔다.
각 방법이 "왜 안 되는지"를 이해하는 과정이 결국 "무엇이 맞는지"를 이해하는 경로가 됐다.

다음 사이클에서 재방문할 키워드:

- `@Transactional` 격리 수준 (REPEATABLE_READ, SERIALIZABLE)
- 분산 환경에서 이 전략들이 어떻게 달라지는가 (분산 락, Redis)

## 5. 다음 타임에 바꿀 것

- 유지할 것과 그 이유
- 바꿀 것과 그 이유
