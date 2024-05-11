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
import roomescape.web.controller.request.ReservationAdminWebRequest;

class ReservationAdminControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("어드민이 예약을 생성한다.")
    void create() {
        LocalDate date = nextDate();
        ReservationAdminWebRequest request =
                new ReservationAdminWebRequest(date.toString(), 1L, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/reservations/"))
                .body("member.name", is("어드민"))
                .body("date", is(date.toString()));
    }

    @Test
    @DisplayName("예약 날짜는 필수이다.")
    void validateDate() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body("{}")
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 날짜는 필수입니다."));
    }

    @Test
    @DisplayName("예약 날짜는 올바른 형식이어야 한다.")
    void validateDateFormat() {
        String invalidDate = "date";
        ReservationAdminWebRequest invalidRequest
                = new ReservationAdminWebRequest(invalidDate, 1L, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body(invalidRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("올바른 날짜 형태가 아닙니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 필수이다.")
    void validateTimeId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body("{}")
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 시간 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 시간 ID는 0보다 커야 한다.")
    void nonPositiveTimeId() {
        Long invalidTimeId = 0L;
        String date = nextDate().toString();
        ReservationAdminWebRequest invalidRequest =
                new ReservationAdminWebRequest(date, 1L, invalidTimeId, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body(invalidRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    @Test
    @DisplayName("예약 멤버 ID는 필수이다.")
    void validateMemberId() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body("{}")
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("예약 멤버 ID는 필수입니다."));
    }

    @Test
    @DisplayName("예약 멤버 ID는 0보다 커야 한다.")
    void nonPositiveMemberId() {
        Long invalidMemberId = 0L;
        String date = nextDate().toString();
        ReservationAdminWebRequest invalidRequest =
                new ReservationAdminWebRequest(date, invalidMemberId, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getAdminToken())
                .body(invalidRequest)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("0보다 커야 합니다"));
    }

    @Test
    @DisplayName("어드민만 접근 가능하다.")
    void onlyAdmin() {
        String date = nextDate().toString();
        ReservationAdminWebRequest request =
                new ReservationAdminWebRequest(date, 1L, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getMemberToken())
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401)
                .body("message", is("유효한 인가 정보를 입력해주세요."));
    }
}
