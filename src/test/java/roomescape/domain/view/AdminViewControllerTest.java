package roomescape.domain.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.ControllerTest;

public class AdminViewControllerTest extends ControllerTest {

    @DisplayName("/admin get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_admin_page() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_reservation_page() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/time get 요청 시 응답할 수 있다")
    @Test
    void should_response_200_when_request_time_page() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
