## 학습 로그 #3

**시간**: 4/29 13:40 ~ 14:30 (약 50분)  
**학습 범위**: 2단계 DB 연동

### 1. 막힌 것의 종류

이번에 막힌 것은 어떤 종류의 어려움이었는가? (해당하는 것에 체크)

- [ ] 개념 자체를 모르겠다 (예: "스프링 빈이 뭔지 모르겠다")
- [x] 개념은 알겠는데 코드로 어떻게 쓰는지 모르겠다 (예: "JdbcTemplate 문법을 모르겠다")
- [ ] 코드는 돌아가는데 이게 맞는 건지 모르겠다 (예: "계층 분리를 이렇게 해도 되나?")
- [ ] 기타: ___

### 2. 이번 타임의 학습 전략

이전 학습법을 토대로 하되, 코드를 설명할 때 줄글로 표현하지 않고, 코드 블록을 사용하거나 도식화하는 전략을 실행했다.

- 학습 과정 예시

````text
## 목표
JdbcTemplate의 개념과 사용 방법을 이해한다.

## 학습 이유
JdbcTemplate을 활용해 데이터베이스에 접근해야 하는데 방식을 모르겠다. 

## 정보 수집
- 공식 문서 - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
	- JDBC 코어 패키지의 핵심 delegate이다. 모든 종류의 JDBC 작업을 지원하며, 다양한 데이터 액세스 목적으로 직접 사용할 수 있다.
	- JDBC 사용을 단순화하고 일반적인 오류를 방지하는 데 도움을 준다. 핵심 JDBC 워크플로우를 실행하므로, 애플리케이션 코드는 SQL을 제공하고 결과를 추출하기만 하면 된다. 
	- 이 클래스는 SQL 쿼리나 업데이트를 실행하고, ResultSet에 대한 반복을 시작하며, JDBC 예외를 포착해 공통 org.springframework.dao 예외 계층 구조로 변환한다. 
	- 이 클래스를 사용하는 코드는 명확하게 정의된 계약을 제공하는 콜백 인터페이스만 구현하면 된다.
	- 이 템플릿의 클래스는 인스턴스 설정이 완료되면 스레드 안전하다. DataSource 참조를 통해 직접 인스턴스화해 서비스 구현 내에서 사용하거나, 애플리케이션 컨텍스트에서 준비해 서비스에 빈 참조로 제공할 수 있다.

- JDBC 코어 패키지란?
	- org.springframework.jdbc.core를 말함

- JdbcTemplate이 JDBC 코어 패키지의 delegate라는 의미는?
	- delegate: 대리자
	- 개발자가 직접 해야할 JDBC 처리를 JdbcTemplate이 대리해서 해준다는 뜻
	- 개발자 입장에서는 delegate(대리자)지만, 내부 로직 관점에서는 다른 객체에 로직을 위임하는 delegator(위임자)이다. 

- 왜 인스턴스 설정이 완료되면 스레드 안전한가?
	- JdbcTemplate 객체를 처음 만들 때는 DateSource(DB 연결 정보) 같은 설정이 필요하다. 설정 단계에서는 객체의 내부 상태가 변하기 때문에, 다른 스레드가 끼어들면 문제가 생길 수 있다. 
	- DataSource 세팅이 끝나면 JdbcTemplate 내부 필드는 더 이상 변하지 않는 읽기 전용이 된다. 

- JdbcTemplate 객체는 new 키워드를 사용해 직접 객체를 만들 수도 있고, 애플리케이션 컨텍스트에서 빈으로 제공할 수도 있다. 
	- 직접 인스턴스화할 때는 DataSource를 주입받아, JdbcTemplate 객체를 생성한다.
	- 빈으로 제공할 때는 스프링 설정 클래스(@Configuration)에서 JdbcTemplate을 미리 만들어두어 빈으로 관리한다. 애플리케이션 전체에서 단 하나의 JdbcTemplate 객체를 공유해 사용한다. 

- JdbcTemplate 핵심 메서드 사용법
	- SELECT
		- 단건 조회: queryForObject() 사용, 결과가 없거나 2개 이상이면 예외 발생
		- 다건 조회: query() 사용
		- RowMapper 전달
	- INSERT / UPDATE / DELETE
		- 모두 update() 사용

- RowMapper란?
	- DB에서 가져온 ResultSet 데이터를 자바 객체로 변환해주는 매핑 규칙
	- 데이터베이스의 한 줄을 자바 객체 한 개로 변환하는 규칙이 필요할 때 전달. SELECT 쿼리를 날려서 데이터를 받아올 때 무조건 필요
	- 인터페이스이므로, 람다식을 사용해 메서드 인자로 바로 전달하는 방식을 주로 사용한다. 

- JdbcTemplate이 없으면 개발자가 얼마나 불편한가?
	- Connection, Statement, ResultSet 등 자원 관리를 직접 해야 한다.
	- 예외 처리를 무조건 해야 한다.
	- while문 루프로 데이터를 한 줄씩 꺼내 가져와야 한다.
	- SQL에 들어갈 변수를 세팅하는 파라미터 바인딩 작업을 직접 해야한다.
	
## 나중에 더 알아볼 것 (깊이 제한)
- 콜백 인터페이스가 무엇인가?
- DataSource란?

## 나만의 언어 (구체적 예시 포함)
JdbcTemplate은 JDBC 사용을 편리하게 해주는 도구이다. 
리소스 관리, 파라미터 바인딩과 예외 처리 등을 자동으로 해준다. 

- JdbcTemplate 메서드 사용 방법
query, queryForObject의 경우 sql문, rowMapper, 파라미터 바인딩에 필요한 값을 순서대로 넣어준다.  

```
- queryForObject("SELECT * FROM member WHERE id = ?", rowMapper(), id);
- query("SELECT * FROM member", rowMapper())
```

update의 경우 sql문, 파라미터 바인딩에 필요한 값을 순서대로 넣어준다.

```
update("DELETE FROM member WHERE id = ?", id)
```

- RowMapper 사용 방법
rowMapper를 전달하는 부분에 람다식을 사용한다.
ResultSet과 현재 행을 매개변수로 전달하고, 객체를 생성한다. 
JdbcTemplate이 rowMapper를 함수형 인터페이스로 구현해 전달한다는 것을 알고 있다. 

```
jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
    Member member = new Member();
    member.setId(rs.getLong("id"));
    member.setName(rs.getString("name"));
    member.setEmail(rs.getString("email"));
    return member;
}, id);
```

## 나만의 언어 검증
- queryForObject는 결과가 정확히 1개가 아니면 예외가 발생한다는 걸 강조
- Checked Exception을 Unchecked Exception으로 변환해줘서 throws 처리를 하지 않아도 되게 해줌을 강조 

## 결론
JdbcTemplate은 JDBC 사용을 편리하게 해주는 도구이다. 
리소스 관리, 파라미터 바인딩 등을 자동으로 해주고, Checked Exception을 Unchecked Exception으로 변환해 강제로 예외 처리하는 상황을 막아준다. 

JdbcTemplate 메서드 사용 방법
query, queryForObject의 경우 sql문, rowMapper, 파라미터 바인딩에 필요한 값을 순서대로 넣어준다.  
queryForObject는 결과가 정확히 1개가 아니면 예외가 발생한다. 

```
- queryForObject("SELECT * FROM member WHERE id = ?", rowMapper(), id);
- query("SELECT * FROM member", rowMapper())
```

update의 경우 sql문, 파라미터 바인딩에 필요한 값을 순서대로 넣어준다.

```
update("DELETE FROM member WHERE id = ?", id)
```

- RowMapper 사용 방법
JdbcTemplate 메서드의 rowMapper를 전달하는 부분에 람다식을 사용한다.
ResultSet과 현재 행을 매개변수로 전달하고, 객체를 생성한다. 
JdbcTemplate이 람다식을 받으면, rowMapper를 함수형 인터페이스로 구현해 전달한다고 인식해 동작한다. 

```
jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
    Member member = new Member();
    member.setId(rs.getLong("id"));
    member.setName(rs.getString("name"));
    member.setEmail(rs.getString("email"));
    return member;
}, id);
```

````

### 3. 전략 평가

- 효과적이었던 것과 그 이유
    - 개념을 깊게 학습하면서 JdbcTemplate과 RowMapper의 역할을 이해하는 데 도움이 됐다.
- 비효과적이었던 것과 그 이유
    - 30분을 학습 시간 제한으로 뒀는데 학습을 하다보니 알아야 할 게 많아져서 50분이 소요됐다.
- 막힌 것의 종류(1번)와 전략의 궁합은 어땠는가?
    - 사용 방법을 주로 알고 싶었는데, 부족했던 기본 개념부터 학습하다보니 시간이 꽤 걸려서 궁합은 그저 그런 것 같다.

### 4. AI 피드백

- 자신의 학습 전략에 대해 AI 학습 전문가에게 피드백을 요청하고,
  유용했던 제안 1가지 이상 기록

```text
- '30분 규칙'이 깨진 이유와 처방법
50분이 걸린 건 당신이 '공부할 것'과 '참고할 것'을 구분하지 못했기 때문일 확률이 높습니다.

전략 개선안: "Driving First"
차를 운전하는 법을 배울 때 엔진의 피스톤 운동 원리부터 배우지 마세요.

1. 일단 JdbcTemplate을 쓰는 가장 간단한 코드를 복사해서 내 프로젝트에 넣습니다. (5분)
2. 빨간 줄(에러)이 뜨면 그 해결책을 찾습니다. "아, DataSource가 없네? 그게 뭐지?" (10분)
3. 코드가 돌아가면 그때 "왜 이게 가능하지?"를 딱 한 가지만 깊게 팝니다. (15분)

이렇게 하면 30분 안에 '동작하는 코드'와 '핵심 개념 1개'를 모두 얻을 수 있습니다.
```

### 5. 다음 타임에 바꿀 것

- 유지할 것과 그 이유
    - 개념을 학습할 때는 기존 학습법이 도움이 되기 때문에 유지할 것이다.
- 바꿀 것과 그 이유
    - 코드 구현 방법을 주로 알고 싶을 때는 최소한의 동작 원리만 파악하고 코드에 적용한 후, 막히거나 생소한 부분을 학습하는 전략을 사용할 것이다.  
      우선순위가 코드 구현인데 개념 학습부터 시작하면 개념 공부에 시간을 많이 쏟게 되기 때문이다. 
