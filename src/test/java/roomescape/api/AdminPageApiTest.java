package roomescape.api;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.dto.LoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminPageApiTest {

    @LocalServerPort
    int port;
    Cookie adminCookie;

    @BeforeEach
    void 쿠키_생성() {
        LoginRequest loginRequest = new LoginRequest("admin@admin.com", "adminadmin");
        adminCookie = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/login")
                .getDetailedCookie("token");
    }

    @Test
    void 메인_페이지_이동() {
        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_페이지_이동() {
        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_시간_페이지_이동() {
        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 테마_페이지_이동() {
        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 관리자_예약() {
        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
