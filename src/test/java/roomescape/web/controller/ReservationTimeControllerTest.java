package roomescape.web.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.service.request.ReservationTimeRequest;
import roomescape.support.IntegrationTestSupport;

class ReservationTimeControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 시간 목록을 조회한다.")
    void getAll() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("예약 가능 시간 목록을 조회한다.")
    void availableTimes() {
        RestAssured.given().log().all()
                .when().get("/times/available?date=2023-05-04&themeId=" + 테마_1번_ID)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("alreadyBooked", contains(true, true, false));
    }

    @Test
    @DisplayName("예약 가능 시간 목록 조회 시 주어지는 날짜는 올바른 형식이어야 한다.")
    void validateDateFormat() {
        String invalidDate = "2023-05-044";

        RestAssured.given().log().all()
                .when().get("/times/available?date=" + invalidDate + "&themeId=" + 테마_1번_ID)
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 날짜 형태가 아닙니다."));
    }

    @Test
    @DisplayName("예약 가능 시간 목록 조회 시 주어지는 테마 ID는 0보다 커야 한다.")
    void validateThemeId() {
        String nonPositive = "0";

        RestAssured.given().log().all()
                .when().get("/times/available?date=2023-05-04&themeId=" + nonPositive)
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        ReservationTimeRequest request = new ReservationTimeRequest("18:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/times/"))
                .body("startAt", is("18:00"));
    }

    @Test
    @DisplayName("중복 예약 시간은 생성할 수 없다.")
    void duplicated() {
        ReservationTimeRequest request = new ReservationTimeRequest("11:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 예약 시간이 존재합니다."));
    }

    @Test
    @DisplayName("생성할 예약 시간 값은 필수이다.")
    void validateStartAt() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 시간은 필수입니다."));
    }

    @Test
    @DisplayName("생성할 예약 시간은 올바른 형식이어야 한다.")
    void validateStartAtFormat() {
        ReservationTimeRequest invalidRequest = new ReservationTimeRequest("102:33");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 시간 형태가 아닙니다."));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/times/" + 예약_시간_3번_ID)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("사용되고 있는 시간은 삭제할 수 없다.")
    void usedDelete() {
        RestAssured.given().log().all()
                .when().delete("/times/" + 예약_시간_2번_ID)
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 시간을 사용하고 있는 예약이 존재합니다."));
    }
}
