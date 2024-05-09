package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.service.request.MemberLoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminPageIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("admin@gmail.com", "admin"))
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    @DisplayName("\"/admin\"으로 GET 요청을 보낼 수 있다.")
    void openAdminPage() {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자 권한이 없으면 \"/admin\"으로 GET 요청을 보낼 수 없다.")
    void openAdminPageWithInvalidToken() {
        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("user1@gmail.com", "user1"))
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("\"/admin/reservation\"으로 GET 요청을 보낼 수 있다.")
    void openAdminReservationPage() {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/time\"으로 GET 요청을 보낼 수 있다.")
    void openAdminTimePage() {
        RestAssured.given().log().all()
                .cookies("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
