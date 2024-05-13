package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    private static String token;

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
        Map<String, String> loginParams = Map.of("email", "user_test@example.com", "password", "password1!");
        token = RestAssured.given().log().all()
                .when().body(loginParams)
                .contentType(ContentType.JSON).post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    @DisplayName("방탈출 어드민 메인 페이지 조회를 확인한다")
    void showAdminMainPage() {
        RestAssured.given().log().all()
                .when().cookie("token", token).get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 예약 관리 페이지 조회를 확인한다")
    void showReservationPage() {
        RestAssured.given().log().all()
                .when().cookie("token", token).get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 예약 시간 관리 페이지 조회를 확인한다")
    void showReservationTimePage() {
        RestAssured.given().log().all()
                .when().cookie("token", token).get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 테마 관리 페이지 조회를 확인한다")
    void showThemePage() {
        RestAssured.given().log().all()
                .when().cookie("token", token).get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
