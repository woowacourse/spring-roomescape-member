# 🧾 미션 중 기록

## 필수 기록

### 규칙을 적용해서 변경한 코드 1곳 이상

예외에 에러 메시지와 상태 코드, 발생 위치를 담아  
클라이언트에게 보내되, 사용자에겐 에러 메시지만을 전달할 것이라 가정하고  
에러 메시지가 문제의 원인과 대응을 잘 드러낼 수 있도록 작성

```java

@RestControllerAdvice
public class ProblemDetailsAdvice {

```

대응은 해당 요청-응답 사이클 내부에서 확인된 정보만을 담는다.  
대응을 위해 추가적인 서버 로직이 동작하지 않도록 한다.

### 변경/취소에서 발견한 엣지 케이스와 처리 방향

`지난 예약` 에 따른 다양한 엣지 케이스

- 이미 지난 예약에 대한 변경/취소
- 이미 지난 날짜로의 예약 변경

`중복 예약` 에 따른 엣지 케이스

- 이미 해당 날짜/시간대/테마로의 변경
- 해당 날짜/시간대/테마 에서 예약자 이름 변경

#### `처리 방향`

요구사항과 원칙을 생각하고 적용하면 알아서 처리됨

### 지금까지의 규칙 중 유지/수정/폐기한 항목

- 대부분의 규칙 유지.
- 리소스 식별의 관점 구체화
- 관리자/사용자 API 분리하되 사용자의 접근 가능성이 없으면 분리하지 않음
- 에러 메시지의 대상과 내용을 통합된 규칙으로 관리

### 특이점

```java

@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
public ResponseEntity<ProblemDetail> handleMethodNotSupportedException(
        HttpRequestMethodNotSupportedException exception) {
    String detailMessage = "해당 경로에서는 " + exception.getMethod() + " 요청을 지원하지 않습니다. API 명세를 확인해 주세요.";

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, detailMessage);
    problemDetail.setProperty("code", "METHOD_NOT_ALLOWED");

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);
}
```

지원하지 않는 엔드포인트 + 메서드 응답을 처리하는 과정에서

```text
2026-05-15T15:54:52.974+09:00 ERROR 11136 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'handlerExceptionResolver' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Failed to instantiate [org.springframework.web.servlet.HandlerExceptionResolver]: Factory method 'handlerExceptionResolver' threw exception with message: Ambiguous @ExceptionHandler method mapped for [ExceptionHandler{exceptionType=org.springframework.web.HttpRequestMethodNotSupportedException, mediaType=*/*}]: {public org.springframework.http.ResponseEntity roomescape.exception.ProblemDetailsAdvice.handleMethodNotSupportedException(org.springframework.web.HttpRequestMethodNotSupportedException), public final org.springframework.http.ResponseEntity org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler.handleException(java.lang.Exception,org.springframework.web.context.request.WebRequest) throws java.lang.Exception}
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657) ~[spring-beans-6.2.5.jar:6.2.5]
```

위와 같은 에러 발생.

#### `원인`

`ProblemDetailsAdvice` 가 `ResponseEntityExceptionHandler` 를 상속받았기 떄문에  
`HttpRequestMethodNotSupportedException` 에 대한 핸들러가 이미 부모 클래스에 존재해  
추가한 핸들러와 충돌.
