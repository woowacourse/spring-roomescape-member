package roomescape.global.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WebMvcConfigTest {

    @Test
    @DisplayName("ADMIN 예약 관리 메인 페이지를 렌더링한다")
    void displayAdminMainPage() {
        RestAssured.given().log().all()
                .when().get("admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 목록 페이지를 렌더링한다")
    void displayAdminReservationPage() {
        RestAssured.given().log().all()
                .when().get("admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 시간 페이지를 렌더링한다")
    void displayAdminTimePage() {
        RestAssured.given().log().all()
                .when().get("admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 테마 페이지를 렌더링한다")
    void displayAdminThemePage() {
        RestAssured.given().log().all()
                .when().get("admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("User 메인 페이지를 렌더링한다")
    void displayMainPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("User 예약 목록 페이지를 렌더링한다")
    void displayReservationPage() {
        RestAssured.given().log().all()
                .when().get("reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지를 렌더링한다")
    void displayLoginPage() {
        RestAssured.given().log().all()
                .when().get("login")
                .then().log().all()
                .statusCode(200);
    }
}
