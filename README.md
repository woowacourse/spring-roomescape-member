### 기능명세서

**화면**

- [ ]  사용자는 화면을 통해 예약을 할 수 있다

**1단계**

- [ ]  모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
- [x]  테마는 이름, 설명, 썸네일 이미지 url을 가진다.
- [x]  예약에 테마 정보를 포함한다.
- [x]  관리자가 테마를 추가할 수 있다.
- [x]  관리자가 아닐경우 예외를 던진다.
- [x]  관리자가 테마를 삭제할 수 있다.
- [x]  존재하지 않는 테마는 삭제할 수 없다.
- [x]  예약이 걸려있는 테마는 삭제할 수 없다.

**API 명세서**
```markdown
Theme - 01
API 설명: 관리자가 테마를 추가할 수 있다.
URI: /api/v1/themes
Method: POST
Path Variable: 
Query Variable:
RequestBody:
{
	"name": String,
	"description" : String,
	"imgUrl" : String,
	"userName" : String,
}
ResponseBody:
{
	"id" : Long,
	"name": String,
	"description" : String,
	"imgUrl" : String,
}
Status Code: 201

Theme - 02
API 설명: 관리자가 테마를 삭제할 수 있다.
URI: /api/v1/themes/{id}
Method: DELETE
Path Variable: themeId
Query Variable: 
RequestBody:
{ 
	"userName" : String
}
ResponseBody:
Status Code: 204

- 이유
- theme에 필요한 값들을 요청 필드로 받아서 post 요청을 보냈고, 
반환 값에서는 id를 추가해줘서 했다. 중간에 userName을 추가한 이유는 admin을 확인하기 위함이다.
- delete도 현재 userName을 식별하여 admin일 때만 삭제할 수 있게 했다.
```

**2단계**

- [x]  사용자가 **날짜와 테마를 선택** 하면 예약 가능한 시간을 조회할 수 있다.
- [x]  예약 가능한 시간이란, 관리자가 등록한 시간 중 해당 날짜+테마에 아직 예약이 없는 시간이다.
- [ ]  사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다.
- [ ]  같은 날짜·시간이라도 테마가 다르면 각각 예약 가능하다.

**API 명세서**

```markdown
Times - 01
API 설명: 사용자가 예약 가능한 시간을 조회할 수 있다.
URI: /api/v1/times?date=2026-05-08&themeId=1
Method: GET
Path Variable: 
Query Variable: date, themeId
RequestBody:
{
}
ResponseBody:
{
	"availableTime" [
		{ 
			"id" : 1,
			"startAt" : 10:00,
			"available" : true
		},
		...
	]
}
Status Code: 200

- 이유: 토론 활동에서 정한 규칙을 적용했습니다.

(If-Then) 만약 사용자가 실제로 조회하고 싶은 대상이 있고, 다른 값들은 그 대상을 좁히기 위한 조건이라면
         → 조회 대상은 리소스로 두고, 나머지 값들은 쿼리 파라미터로 표현한다.
         (이유: 리소스는 사용자가 받고 싶은 핵심 대상을 기준으로 식별하고, 조건 값들은 그 결과를 필터링하는 역할이기 때문이다.)
         
(If-Then) 만약 어떤 값의 판단은 서버만 할 수 있지만, 그 결과를 어떻게 보여줄지는 클라이언트가 결정해도 된다면
         → 서버는 판단 결과를 포함한 원본 데이터를 내려주고, 클라이언트가 이를 기준으로 필터링하거나 표시 방식을 결정한다.
         (이유: 정합성과 비즈니스 규칙 판단은 서버의 책임이고, 화면 표현과 사용자 경험은 클라이언트의 책임이기 때문이다.)

```
