package roomescape.controller.page;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserPageTest {

    @Test
    @DisplayName("200 상태코드와 메인 페이지를 반환한다")
    void returnMainPageWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("200 상태코드와 사용자 예약 페이지를 반환한다")
    void returnReservationPageWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
