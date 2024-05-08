package roomescape.acceptance;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class AdminAcceptanceTest extends AcceptanceTest {
    @Test
    void 어드민_메인_페이지를_응답할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약_관리_페이지를_응답할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 시간_관리_페이지를_응답할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
