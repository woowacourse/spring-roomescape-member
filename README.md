# 1단계 - 방탈출 예약 관리

## 구현할 기능 목록

* 예약 조회, 추가, 삭제

## 방탈출 예약 관리 비즈니스 정의

* 방탈출 고객이 관리자를 통해 방탈출 예약을 한다.
* 현재 방탈출은 한가지 카테고리의 방탈출만 존재한다.
    * 나중에 다른 유형의 방탈출이 추가 될 수 있다.
* 방탈출은 게임마다 시간이 다르다.
    * 현재는 한 가지 카테고리만 존재하므로 1시간으로 고정한다.

## 객체 명세서 V1. 방탈출 예약 관리

### ReservationTime (예약 시간 - VO)
> 상태
* startAt: LocalTime (시작 시간)

> 행위
* toDateTime(LocalDate date): LocalDateTime
    * 특정 날짜와 결합하여 일시 정보를 생성한다.

### Reservation (예약 - Entity)
> 상태
* name: String (예약자 이름)
* date: LocalDate (예약 날짜)
* time: ReservationTime (예약 시간 정보)
