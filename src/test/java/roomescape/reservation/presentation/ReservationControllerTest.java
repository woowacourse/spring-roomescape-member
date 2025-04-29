package roomescape.reservation.presentation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.presentation.fixture.ReservationFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {
    ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("예약 추가 테스트")
    void createReservationTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when-then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "aaaaaaaaaaa"})
    @DisplayName("예약자명이 존재하지 않거나, 10자를 초과할 수 없다.")
    void createReservationNameExceptionTest(String name){
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation(name, "2025-08-05", "1");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("날짜는 LocalDate 형식을 만족시켜야 한다.")
    void createReservationDateExceptionTest(){
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-0805", "1");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void createReservationIsPastDateExceptionTest(){
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2024-08-05", "1");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 일시의 예약은 불가능하다.")
    void createReservationIsDuplicateDateExceptionTest(){
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 ID를 이용하여 예약할 수 없다.")
    void createReservationInvalidTimeIdExceptionTest(){
        // given
        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 조회 테스트")
    void reservationPageTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when-then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}