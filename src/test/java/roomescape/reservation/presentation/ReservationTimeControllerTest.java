package roomescape.reservation.presentation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.presentation.fixture.ReservationFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {
    private ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("시간 추가 테스트")
    void createTimeTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");

        // when-then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("시작 시간은 LocalTime 형식을 만족시켜야 한다.")
    void createTimeExceptionTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10-00");

        // when-then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 시간 추가는 불가능하다.")
    void createTimeDuplicateExceptionTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시간 조회 테스트")
    void getTimesTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // when-then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 가능 시간 조회 테스트")
    void getAvailableTimesTest() {
        // given
//        Map<String, String> availableReservationTime = reservationFixture.createReservationTime("10:00");
        Map<String, String> unAvailableReservationTime = reservationFixture.createReservationTime("11:00");

//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(availableReservationTime)
//                .when().post("/times");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(unAvailableReservationTime)
                .when().post("/times");

        Map<String, String> theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1", "2");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when
        RestAssured.given().log().all()
                .when().get("/times/available?date=2025-08-05&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("alreadyBooked", is(true));
    }

    @Test
    @DisplayName("시간 삭제 테스트")
    void deleteTimeTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        // when-then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 이미 존재하는 시간은 삭제할 수 없다.")
    void deleteTimeExceptionTest() {
        // given
        Map<String, String> reservationTime = reservationFixture.createReservationTime("10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");

        Map<String, String> theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes");

        Map<String, String> reservation = reservationFixture.createReservation("브라운", "2025-08-05", "1", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when-then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

}
