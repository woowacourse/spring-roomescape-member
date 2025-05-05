package roomescape.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class AdminControllerTest {

    @DisplayName("어드민 메인 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Main() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 내역 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Reservation() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 시간 관리 페이지를 출력한다")
    @Test
    void checkAdminDisplay_ReservationTime() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 테마 관리 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Theme() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
