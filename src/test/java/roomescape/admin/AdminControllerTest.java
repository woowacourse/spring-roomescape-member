package roomescape.admin;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.util.ControllerTest;

@DisplayName("관리자 페이지 테스트")
class AdminControllerTest extends ControllerTest {
    @DisplayName("관리자 메인 페이지 조회에 성공한다.")
    @Test
    void adminMainPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200)
                .body(containsString("방탈출 어드민"));
    }

    @DisplayName("관리자 예약 페이지 조회에 성공한다.")
    @Test
    void adminReservationPage() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString("방탈출 예약 페이지"));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("관리자 시간 페이지 조회에 성공한다.")
    @Test
    void adminTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200)
                .body(containsString("시간 관리 페이지"));
    }

    @DisplayName("관리자 테마 페이지 조회에 성공한다.")
    @Test
    void adminThemePage() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200)
                .body(containsString("테마 관리 페이지"));
    }
}
