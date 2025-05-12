package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void test1() {
        String tokenValue = getAdminLoginTokenValue();

        RestAssured.given().log().all()
                .cookie("token", tokenValue)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void test2() {
        String tokenValue = getAdminLoginTokenValue();

        RestAssured.given().log().all()
                .cookie("token", tokenValue)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("모든 예약 정보를 반환한다.")
    @Test
    void test3() {
        addReservationTime("10:00");
        addTheme();
        String tokenValue = getAdminLoginTokenValue();

        RestAssured.given()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "date", LocalDate.now().plusDays(1L),
                        "timeId", 1,
                        "themeId", 1
                ))
                .when().post("/reservations")
                .then();

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test4() {
        addReservationTime("10:00");
        addTheme();
        String tokenValue = getAdminLoginTokenValue();
        Map<String, Object> reservationParams = Map.of(
                "date", LocalDate.now().plusDays(1L),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("존재하지 않는 예약 시간 ID 를 추가하면 예외를 반환한다.")
    @Test
    void test5() {
        String tokenValue = getAdminLoginTokenValue();
        addTheme();
        Map<String, Object> reservationParams = Map.of(
                "date", LocalDate.now().plusDays(1L),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("존재하지 않는 테마 ID 를 추가하면 예외를 반환한다.")
    @Test
    void notExistThemeId() {
        String tokenValue = getAdminLoginTokenValue();
        addReservationTime("10:00");
        Map<String, Object> reservationParams = Map.of(
                "date", LocalDate.now().plusDays(1L),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void test6() {
        addTheme();
        addReservationTime("10:00");
        String tokenValue = getAdminLoginTokenValue();
        Map<String, Object> reservationParams = Map.of(
                "date", LocalDate.now().plusDays(1L),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then();

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 예약을 삭제할 경우 NOT_FOUND 반환")
    @Test
    void test7() {
        RestAssured.given().log().all()
                .when().delete("/reservations/4")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("예약 가능한 시간을 반환한다")
    @Test
    void test9() {
        addReservationTime("10:00");
        addReservationTime("11:00");
        addTheme();
        String tokenValue = getAdminLoginTokenValue();
        LocalDate day = LocalDate.now().plusDays(1L);
        Map<String, Object> reservationParams = Map.of(
                "date", day,
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given()
                .cookie("token", tokenValue)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then();

        RestAssured.given().log().all()
                .when().get("/reservations/available?date=" + day + "&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("alreadyBooked", containsInAnyOrder(true, false));
    }

    private String getAdminLoginTokenValue() {
        Map<String, String> adminLoginParams = Map.of("email", "admin@woowa.com", "password", "12341234");
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminLoginParams)
                .when().post("/login")
                .then()
                .extract().cookie("token");
    }

    private void addReservationTime(final String timeValue) {
        Map<String, String> timeParams = Map.of("startAt", timeValue);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then();
    }

    private void addTheme() {
        Map<String, String> themeParams = Map.of(
                "name", "테마1", "description", "테마1", "thumbnail", "www.m.com"
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then();
    }
}
