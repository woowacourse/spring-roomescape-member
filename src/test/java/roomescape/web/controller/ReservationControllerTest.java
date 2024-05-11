package roomescape.web.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.time.LocalDate;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;
import roomescape.web.controller.request.ReservationWebRequest;

class ReservationControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    void getAll() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("특정 사용자가 예약한 주어진 기간 동안의 예약 목록을 조회한다.")
    void getAllByMemberId() {
        RestAssured.given().log().all()
                .when().get("/reservations?memberId=1&fromDate=2023-05-01&endDate=2023-05-04")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("특정 기간의 예약 목록을 조회한다.")
    void getAllByPeriod() {
        RestAssured.given().log().all()
                .when().get("/reservations?fromDate=2023-05-01&endDate=2023-05-04")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("검색 대상 예약의 날짜는 올바른 형식이어야한다.")
    void getAllByInvalidThemeId() {
        String invalidQueryParameters = "fromDate=2022-222-22&endDate=2022-22-12";
        RestAssured.given().log().all()
                .when().get("/reservations?" + invalidQueryParameters)
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 날짜 형태가 아닙니다."));
    }

    @Test
    @DisplayName("사용자가 예약을 생성한다.")
    void create() {
        LocalDate date = nextDate();
        ReservationWebRequest request = new ReservationWebRequest(date.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/reservations/"))
                .body("member.name", is("일반 사용자"))
                .body("date", is(date.toString()));
    }

    @Test
    @DisplayName("예약 날짜는 필수이다.")
    void validateDate() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 날짜는 필수입니다."));
    }

    @Test
    @DisplayName("예약 날짜는 올바른 형식이어야 한다.")
    void validateDateFormat() {
        String invalidDate = "date";
        ReservationWebRequest invalidRequest = new ReservationWebRequest(invalidDate, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 날짜 형태가 아닙니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 필수이다.")
    void validateTimeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 시간 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 0보다 커야 한다.")
    void nonPositiveTimeId() {
        Long invalidTimeId = 0L;
        String date = nextDate().toString();
        ReservationWebRequest invalidRequest = new ReservationWebRequest(date, invalidTimeId, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    @Test
    @DisplayName("존재하지 않는 시간 ID에 대한 예약을 할 수 없다.")
    void nonExistTimeId() {
        Long nonExistTimeId = 4L;
        String date = nextDate().toString();
        ReservationWebRequest invalidRequest = new ReservationWebRequest(date, nonExistTimeId, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당되는 예약 시간이 없습니다."));
    }

    @Test
    @DisplayName("예약 테마 ID는 필수이다.")
    void validateThemeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 테마 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 테마 ID는 0보다 커야 한다.")
    void nonPositiveThemeId() {
        Long invalidThemeId = 0L;
        String date = nextDate().toString();
        ReservationWebRequest invalidRequest = new ReservationWebRequest(date, 1L, invalidThemeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    @Test
    @DisplayName("존재하지 않는 테마 ID에 대한 예약을 할 수 없다.")
    void nonExistThemeId() {
        Long nonExistThemeId = 3L;
        String date = nextDate().toString();
        ReservationWebRequest invalidRequest = new ReservationWebRequest(date, 1L, nonExistThemeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당되는 테마가 없습니다."));
    }

    @Test
    @DisplayName("중복 예약을 생성할 수 없다.")
    void duplicated() {
        String date = nextDate().toString();
        ReservationWebRequest request = new ReservationWebRequest(date, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("중복된 예약이 존재합니다."));
    }

    @Test
    @DisplayName("지나간 시간에 대한 예약을 할 수 없다.")
    void previousDateTime() {
        String previousDate = previousDate().toString();
        ReservationWebRequest request = new ReservationWebRequest(previousDate, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지나간 시간에 대한 예약을 할 수 없습니다."));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
