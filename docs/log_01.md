# 학습 로그 #01

**시간**: 05/08 17:00 ~ 17:40 (약 40분)
**학습 범위**: Content-Type

## 1. 막힌 것의 종류

이번에 막힌 것은 어떤 종류의 어려움이었는가? (해당하는 것에 체크)

- [ ] 개념 자체를 모르겠다 (예: "스프링 빈이 뭔지 모르겠다")
- [ ] 개념은 알겠는데 코드로 어떻게 쓰는지 모르겠다 (예: "JdbcTemplate 문법을 모르겠다")
- [ ] 코드는 돌아가는데 이게 맞는 건지 모르겠다 (예: "계층 분리를 이렇게 해도 되나?")
- [x] 기타: @ResponseBody를 사용하면 application/json이 반환될 것이다.

## 2. 이번 타임의 학습 전략

- 이전에 바꾸기로 한 전략은 무엇이었고, 실행했는가?
- 실제로 어떻게 학습했는지 디테일한 과정을 써보세요.

## 이전 학습 내용

- @RestController를 사용하면 @ResponseBody, @Controller가 선언된다.
- @ResponseBody는 메타 데이터이고, ReturnValue 어쩌고 저쩌고에서 제이슨으로 반환해준다.

## 이번 실험 내용

@ResponseBody 어노테이션을 사용하면 객체를 반환할때 application/json 형식으로 반환할 것이다.

### 실험 1 : String

- **예상 결과**:`Content-Type: application/json`

- **실제 결과** :`Content-Type: text/plain`

application/json이 반환될 것 이라 기대했으나 결과는 text/plain이다. -> ???

@ResponseBody 메타데이터를 확인하고 바로 Json으로 반환하지 않는다.

### 실험 2 : Integer

- **예상 결과** :`Content-Type: application/json`

- **실제 결과** : `Content-Type: application/json`

### 실험 3: int

- **예상 결과** :`Content-Type: application/json`

- **실제 결과** : `Content-Type: application/json`

### 실험 4: String 반환, Accept: application/json

- **예상 결과** : `Content-Type: application/json`

- **실제 결과** : `Content-Type: application/json`

Accept 우선순위가 제일 먼저이다.

### 결론

AI를 이용해서 궁금한 부분 학습

@ResponseBody가 붙는다고 바로 Json 형식으로 전달하지 않는다.
@ResponseBody로 인해서 RequestResponseBodyMethodProcessor가 선택되고, 클라이언트에서 요청한 우선순위에 따라서 타입을 선택한다.

```
DispatcherServlet
    → HandlerAdapter (RequestMappingHandlerAdapter)
        → ReturnValueHandler 목록을 순회 
            → RequestResponseBodyMethodProcessor가 @ResponseBody를 보고 "내가 처리할게" 
                → MessageConverter 목록을 순회해서 적절한 Converter에게 위임
                    → StringHttpMessageConverter or Jackson...
```

만약 협상 우선순위가 동일하다면, 아래와 같은 순서대로 반환타입과 매칭되는 것을 우선적으로 반환한다.
그래서 String 타입이면 text/plain 으로 전달했던 것이다.

궁금해서 byte[] 전달을 해봤는데, application/octet-stream 타입을 전달한다.

```
ByteArrayHttpMessageConverter — byte[]
StringHttpMessageConverter — String ← 여기서 String이 낚아채임
ResourceHttpMessageConverter — Resource
ResourceRegionHttpMessageConverter
AllEncompassingFormHttpMessageConverter — form data
MappingJackson2HttpMessageConverter — 객체 → JSON
```

## 결론

@ResponseBody는 메타 데이터이고, ReturnValueHandler에서 RequestResponseBodyMethodProcessor가 선택된다.
그리고 MessageConverter에서 협상 우선순위에 따라 조회를 하면서 보낸다.
만약 우선순위가 없다면, 위 순서대로 Content-Type을 선택해서 전달한다.

## 3. 전략 평가

- 효과적이었던 것과 그 이유
- 비효과적이었던 것과 그 이유
- 막힌 것의 종류(1번)와 전략의 궁합은 어땠는가?

## 4. AI 피드백

- 자신의 학습 전략에 대해 AI 학습 전문가에게 피드백을 요청하고,
  유용했던 제안 1가지 이상 기록

## 5. 다음 타임에 바꿀 것

- 유지할 것과 그 이유
- 바꿀 것과 그 이유
