package roomescape.admin.presentation.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.ApiHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminViewControllerTest {

    @Test
    @DisplayName("어드민 페이지 테스트")
    void adminTest() {
        String token = ApiHelper.getAdminToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 페이지 테스트")
    void reservationPageTest() {
        String token = ApiHelper.getAdminToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 시간 페이지 테스트")
    void reservationTimePageTest() {
        String token = ApiHelper.getAdminToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 페이지 테스트")
    void themePageTest() {
        String token = ApiHelper.getAdminToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 아닐 경우 관리자 페이지에 접근할 수 없다.")
    void getAdminPageFailedTest() {
        String token = ApiHelper.getUserToken();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }
}
