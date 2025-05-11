package roomescape.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class AdminViewControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification requestSpec;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"wooteco@gmail.com\",\"password\":\"1234A\"}")
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");

        requestSpec = RestAssured.given()
                .cookie("token", cookie);
    }

    @DisplayName("어드민 메인 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Main() {
        requestSpec.log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 내역 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Reservation() {
        requestSpec.log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 시간 관리 페이지를 출력한다")
    @Test
    void checkAdminDisplay_ReservationTime() {
        requestSpec.log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 테마 관리 페이지를 출력한다")
    @Test
    void checkAdminDisplay_Theme() {
        requestSpec.log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
