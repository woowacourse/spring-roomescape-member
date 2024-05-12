package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/testdata.sql")
class AdminControllerTest {

    @Autowired
    LoginController loginController;
    String accessToken;

    @BeforeEach
    void setUp() {
        final ResponseEntity<TokenResponse> login = loginController.login(new LoginRequest("789@email.com", "789"), new MockHttpServletResponse());
        accessToken = login.getBody().getAccessToken();
    }

    @Test
    @DisplayName("어드민 페이지를 조회한다.")
    void readAdminPage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("어드민 예약 페이지를 조회한다.")
    void readAdminReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("시간 관리 페이지를 조회한다.")
    void readAdminTimePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 관리 페이지를 조회한다.")
    void readAdminThemePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
