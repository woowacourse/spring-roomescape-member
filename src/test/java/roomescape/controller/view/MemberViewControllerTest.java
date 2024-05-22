package roomescape.controller.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import roomescape.BaseTest;

class MemberViewControllerTest extends BaseTest {

    @Test
    @DisplayName("인기 테마 페이지를 매핑한다.")
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 사용자 예약 페이지를 매핑한다.")
    void reservation() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지를 매핑한다.")
    void login() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }
}
