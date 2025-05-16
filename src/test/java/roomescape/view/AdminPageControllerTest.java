package roomescape.view;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        adminToken = getToken("admin@email.com", "password");
        userToken = getToken("user@email.com", "password");
    }

    private String getToken(String email, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password))
                .when().post("/login")
                .then().statusCode(200)
                .extract().response().getDetailedCookies().getValue("token");
    }

    @DisplayName("어드민 계정으로 어드민 페이지에 접근할 수 있다")
    @Test
    void getAdminPageTest_WhenAdminLogin() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("유저 계정으로 어드민 페이지에 접근할 수 없다")
    @Test
    void getAdminPageTest_WhenUserLogin() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("어드민 예약 관리 페이지를 불러 온다")
    @Test
    void getAdminReservationPageTest() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 시간 관리 페이지를 불러 온다")
    @Test
    void getAdminTimePageTest() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 방 테마 관리 페이지를 불러 온다")
    @Test
    void getAdminThemePageTest() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
