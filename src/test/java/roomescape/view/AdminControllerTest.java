package roomescape.view;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.config.TestConfig;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    @DisplayName("관리자 홈페이지를 정상적으로 반환한다")
    @Test
    void admin_test() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 페이지를 정상적으로 반환한다")
    @Test
    void reservation_test() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 시간 페이지를 정상적으로 반환한다")
    @Test
    void time_test() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

    @DisplayName("관리자 예약 페이지를 정상적으로 반환한다")
    @Test
    void theme_test() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

}
