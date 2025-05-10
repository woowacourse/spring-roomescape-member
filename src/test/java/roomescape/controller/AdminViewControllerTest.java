package roomescape.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dto.request.LoginRequest;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class AdminViewControllerTest {

    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "1234";


    @DisplayName("/admin 페이지 연결 테스트")
    @Test
    void getAdminPagePage() {
        RestAssured.given().log().all()
                .cookie("token",getToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation 페이지 연결 테스트")
    @Test
    void getAdminPageReservationPage() {
        RestAssured.given().log().all()
                .cookie("token",getToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get("admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/time 페이지 연결 테스트")
    @Test
    void getAdminPageTimePage() throws URISyntaxException, IOException {
        RestAssured.given().log().all()
                .cookie("token",getToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get("admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/theme 페이지 연결 테스트")
    @Test
    void getAdminPageThemePage() throws URISyntaxException, IOException {
        RestAssured.given().log().all()
                .cookie("token",getToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get("admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    private String getToken(String email, String password) {
        return RestAssured.given().log().all()
                .body(new LoginRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");
    }
}
