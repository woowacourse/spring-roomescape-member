# Reservation 테이블의 soft delete 방법 결정

## 처음 시도: reservation에 status 속성 추가

처음에는 reservation 테이블에 status 필드를 `RESERVED`에서 `CANCELED`로 변경시켜 예약 취소를 하도록 했다.

```sql
CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    status   VARCHAR(100) NOT NULL -- status 필드
        DEFAULT 'RESERVED',
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (date, time_id, theme_id)
);
```

하지만 이 방식은 unique 제약 때문에 사용자가 특정 테마의 예약 취소 후 동일 날짜와 시간으로 예약할 수 없게되는 문제가 있다.

| id      | name | date           | time_id | theme_id | status   |
| ------- | ---- | -------------- | ------- | -------- | -------- |
| 1       | yk   | 2026-05-15     | 1       | 1        | CANCELED |
| 2(충돌) | yk   | **2026-05-15** | **1**   | **1**    | RESERVED |

## 생각한 방법

찾아봤을 때는 다음의 해결 방법이 떠올랐다.

1. status에 null값 활용하기
2. generated column
3. unique 제약 제거 후 애플리케이션에서 처리
4. 취소 예약 테이블 분리

### status에 null값 활용하기

status에 `RESERVED`나 `NULL`을 넣고, UNIQUE 키를 (date, time_id, theme_id, status)까지 늘리는 방식이다. 대부분의 DB는 unique 인덱스에서 NULL을 서로 다른 값으로 본다는 점을 활용한다.

| id      | name | date           | time_id | theme_id | status   |
| ------- | ---- | -------------- | ------- | -------- | -------- |
| 1       | yk   | 2026-05-15     | 1       | 1        | NULL     |
| 2(가능) | yk   | **2026-05-15** | **1**   | **1**    | RESERVED |

(date, time_id, theme_id)가 같아도 status가 NULL인 행은 여러 개가 함께 존재할 수 있고, 같은 슬롯을 다시 예약해도 충돌이 나지 않는다.

```sql
CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    status   VARCHAR(100) DEFAULT 'RESERVED',
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (date, time_id, theme_id, status)
);
```

장점

- 테이블이 하나로 끝난다.
- 활성 예약만 보고 싶을 때 조건이 `status = 'RESERVED'` 하나면 된다.

단점

- 현재는 상태가 둘 뿐이지만 상태가 더 늘어날 때는 활용할 수 없다.
- 조회 쿼리에 `WHERE status IS NOT NULL` 같은 clause를 붙여야 한다. 한 군데라도 빠지면 취소된 예약이 본인 조회에 끼거나 가능 시간 계산을 흐트러뜨린다.

### generated column

취소 여부에 따라 값이 자동으로 정해지는 컬럼을 하나 두고, 그걸 UNIQUE 키에 끼워 넣는 방식이다.
예를 들어

```sql
cancel_marker BIGINT GENERATED ALWAYS AS
    (CASE WHEN status='CANCELED' THEN id ELSE 0 END)
```

컬럼을 만들고, `UNIQUE (date, time_id, theme_id, cancel_marker)`로 묶는다.
활성 예약은 marker가 모두 0이라 unique 제약이 그대로 걸린다. 취소된 예약은 각자 자기 id를 marker 값으로 가져서 서로 겹칠 일이 없다.

장점

- 취소된 행은 unique 검사에 잡히지 않는다.
- 활성과 취소를 한 테이블 안에서 처리할 수 있다.

단점

- generated column이 어디까지 지원되고 어떻게 동작하는지는 DB마다 다르다. 사용할 DB에서 원하는 구문을 지원하는지 확인해야 한다.
- 비즈니스 로직이 스키마에 섞여서 보인다. cancel_marker가 왜 있는지, 어떻게 계산되는지 이해하려면 generated column의 정의를 봐야 한다. 스키마만 봐서는 예약과 취소가 어떻게 구분되는지 직관적으로 알기 어렵다.

### unique 제약 제거 후 애플리케이션에서 처리

DB에서 `UNIQUE(date, time_id, theme_id)`를 빼고, 중복 검사를 서비스 코드가 전부 책임지는 방식이다.

장점

- 스키마가 깔끔해진다. status 컬럼이나 별도 테이블 없이 soft delete할 수 있다.
- 같은 슬롯에 취소된 행이 남아 있어도 새 예약 활성이 된 행이 들어올 수 있다.

단점

- 정합성의 마지막 책임이 애플리케이션 쪽으로 통째로 넘어온다. DB에서 비즈니스 의도에 맞는 데이터를 방어해 주지 못한다.
- 동시 요청에서 race condition을 막을 수단이 없다. 같은 슬롯에 두 요청이 거의 동시에 들어오면 둘 다 중복 검사를 통과해서 두 행이 같이 INSERT 될 수 있다. 막으려면 락을 추가하거나 트랜잭션 격리 수준을 손봐야 한다.

## 현재 결정: 취소 예약 테이블 분리

활성 예약은 `reservation`에, 취소된 예약은 `canceled_reservation`에 담는다.
사용자가 예약을 취소하면 한 트랜잭션 안에서 `reservation`의 행을 지우고 같은 id로 `canceled_reservation`에 다시 적는다.

```sql
CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (date, time_id, theme_id)
);

CREATE TABLE canceled_reservation
(
    id       BIGINT       NOT NULL,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id)
);
```

선택한 이유

- `reservation`의 `UNIQUE(date, time_id, theme_id)`를 그대로 둘 수 있다. 활성 예약끼리의 중복은 DB가 계속 막아 주고, 취소된 예약은 처음부터 다른 테이블에 있으니 부딪힐 일이 없다.
- 활성 예약을 다루는 모든 쿼리에 status 필터를 끼울 필요가 없다. `reservation`을 보는 순간 그게 곧 활성 예약이니 필터를 깜빡해서 생기는 버그가 사라진다.
- 의도가 스키마에 그대로 보인다. 테이블 두 개가 따로 있다는 사실만으로 활성과 취소를 다르게 다루는 도메인임이 드러난다.
- 취소 이력이 필요해질 때는 `canceled_reservation`에서 데이터를 조작/조회하도록 할 수 있다.

받아들인 단점

- 두 테이블의 스키마가 거의 같아서 사실상 중복이다. reservation의 컬럼이 바뀌면 `canceled_reservation`도 함께 손봐야 한다.
- 취소까지 포함한 전체 이력을 한 번에 보려면 두 테이블을 UNION 해야 한다. 지금은 그런 조회가 없어서 비용이 안 드러나지만, 그런 조회가 필요하면 view 생성도 고려해야 한다.
