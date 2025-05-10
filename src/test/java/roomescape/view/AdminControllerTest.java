package roomescape.view;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.util.fixture.AuthFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private AuthService authService;

    @DisplayName("관리자 홈페이지를 정상적으로 반환한다")
    @Test
    void admin_test() {
        // given
        String token = AuthFixture.createAdminToken(authService);

        // when & then
        RestAssured.given().log().all()
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 페이지를 정상적으로 반환한다")
    @Test
    void reservation_test() {
        // given
        String token = AuthFixture.createAdminToken(authService);

        // when & then
        RestAssured.given().log().all()
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 시간 페이지를 정상적으로 반환한다")
    @Test
    void time_test() {
        // given
        String token = AuthFixture.createAdminToken(authService);

        // when & then
        RestAssured.given().log().all()
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 페이지를 정상적으로 반환한다")
    @Test
    void theme_test() {
        // given
        String token = AuthFixture.createAdminToken(authService);

        // when & then
        RestAssured.given().log().all()
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

}
