## 방탈출 사용자 예약

### 요구사항

- **1 단계**
  - 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 합니다.
      - 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 등등
  - 아래와 같은 서비스 정책을 반영합니다.
    - 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - 중복 예약은 불가능하다.
      - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
  - 어드민의 시간 관리 페이지, 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인합니다.

- **2 단계**
  - 관리자가 테마를 관리할 수 있도록 기능을 추가합니다.
  - 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.

- **3 단계**
  - 관리자가 아닌 사용자가 예약 가능한 시간을 조회하고, 예약할 수 있도록 기능을 추가/변경 합니다.
  - 인기 테마 조회 기능을 추가합니다.

### 세부 요구 사항

#### 관리자 기능

1. 사용자 요청에 대한 예외 처리를 적용한다.

- [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
  - [x] 각 입력에 대하여 빈 입력값을 시도하였을 때
  - [x] 예약자명 예외 처리 
    - [x] 특수문자가 포함된 이름 처리 
  - [x] 날짜 예외 처리
    - [x] 과거 날짜 입력 예외 처리 

2. 관리자 요청에 대한 예외 처리를 적용한다.

- [x] 시작 시간에 유효하지 않은 값이 입력되었을 때 예외로 처리한다.
    - [x] 비어있는 입력값 처리
    - [x] 운영 시간 외의 예약 시간 입력 처리
    - [x] 이미 존재하는 시간에 대한 입력 처리
- [x] 존재하는 예약에 해당하는 시간 삭제 요청 예외 처리

3. 테마를 관리하는 기능을 구현한다.

- [x] 테마 관리 페이지를 반환한다.
- [x] 방탈출 테마를 관리하는 기능을 구현한다.
  - [x] 테마를 조회한다.
  - [x] 테마를 생성한다.
  - [x] 테마를 삭제한다.

#### 사용자 기능
0. 사용자 페이지
- [x] 사용자 페이지 요청시 반환 기능

1. 예약 조회
- [x] 모든 예약 시간을 보여준다.
  - [x] 예약이 이미 존재하는 시간을 구별해서 보여줘야한다.

2. 시간 조회
- [x] 예약 가능한 시간을 조회한다.

3. 테마 목록 조회
- [x] 예약 많은 순으로 테마 순위를 오름차순으로 보여준다.

### 3단계 추가 API 명세서


- 예약 가능 시간 조회

#### Request
> GET /reservations/{themeId}?date= HTTP/1.1
>
> content-type: application/json

#### Response

> HTTP/1.1 200
>
> Content-Type: application/json

```json
[
  {
    "timeId": 1,
    "startAt": "09:35"
  }
]
```

- 인기 테마 목록 조회

### Request
> GET /themes/rank HTTP/1.1
>
> content-type: application/json

### Response

> HTTP/1.1 200
>
> Content-Type: application/json

```json
[
  {
    "name": "polla 테마",
    "thumbnail": "https://cdn.palms.blog/polla/image/0c10be97-f2f1-497e-855f-8f2f83f938f2.jpg",
    "description": "폴라 방탈출"
  },
  {
    "name": "dobby 테마",
    "thumbnail": "https://mblogthumb-phinf.pstatic.net/MjAxOTA3MDJfMjAg/MDAxNTYyMDMyNDU3MjYy.cPtTMpCaonsPlOXPHubzE_j71yVeF7L95OoxXs31lYkg.ZQg5emgUhXDPa7cdrMZh4KktyHk-QXub5LSm-5f13tMg.JPEG.yeorang92/DOBBY_by%ED%99%8D%EC%97%AC%EB%9E%91.jpg?type=w800",
    "description": "도비 방탈출"
  }
]
```
