package roomescape.controller;

import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.infra.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(ReplaceUnderscores.class)
@ActiveProfiles(value = "test")
class AdminViewControllerTest {

    @LocalServerPort
    int port;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = new JwtTokenProvider().createToken(1L, "admin", "ADMIN");
    }

    @Test
    void 관리_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void 관리_예약_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 관리_시간_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 관리_테마_페이지_접근시_정상응답() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
