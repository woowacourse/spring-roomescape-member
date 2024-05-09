package roomescape.controller.admin;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.controller.dto.TokenRequest;

class AdminReservationControllerTest extends IntegrationTestSupport {

    @LocalServerPort
    int port;

    @DisplayName("예약 내역을 필터링하여 조회한다.")
    @Test
    void findReservationByFilter() {
        RestAssured.port = port;

        String adminToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split("=")[1];

        Map<String, String> params = Map.of("themeId", "1",
                "memberId", "1",
                "dateFrom", "2024-05-04",
                "dateTo", "2024-05-04"
        );

        RestAssured.given().log().all()
                .cookies("token", adminToken)
                .queryParams(params)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
