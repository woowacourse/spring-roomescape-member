package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.user.controller.AuthController;
import roomescape.user.dto.request.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(ReplaceUnderscores.class)
@ActiveProfiles(value = "test")
class AdminViewControllerTest {

    @LocalServerPort
    int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        token = getAdminToken();
    }

    @Test
    void 관리_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .when().cookie(AuthController.TOKEN_KEY, token).get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 관리_예약_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .when().cookie(AuthController.TOKEN_KEY, token).get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 관리_시간_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .when().cookie(AuthController.TOKEN_KEY, token).get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 관리_테마_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .when().cookie(AuthController.TOKEN_KEY, token).get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 로그인_테스트() {
        String adminToken = getAdminToken();

        assertThat(adminToken.split("\\.")).hasSize(3);
    }

    private String getAdminToken() {
        return RestAssured
                .given().log().all()
                .body(new LoginRequest("admin1@test.com", "test"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie(AuthController.TOKEN_KEY);
    }
}
