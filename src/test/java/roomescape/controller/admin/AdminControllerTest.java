package roomescape.controller.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.login.TokenRequest;

@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    final TokenRequest request = new TokenRequest("admin@test.com", "admin");

    @LocalServerPort
    int port;

    Cookie token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        token = RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract()
                .detailedCookie("token");
    }

    @Test
    @DisplayName("어드민 홈 페이지를 응답한다.")
    void getAdminPage200() {
        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("어드민 예약 시간 페이지를 응답한다.")
    void getTimePage200() {
        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("어드민 예약 페이지를 응답한다.")
    void getReservationPage200() {
        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("어드민 테마 페이지를 응답한다.")
    void getThemePage200() {
        RestAssured.given().log().all()
                .cookie(token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}