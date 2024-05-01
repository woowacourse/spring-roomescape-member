package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PageAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("어드민 페이지를 조회한다.")
    void adminPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 예약 페이지를 조회한다.")
    void adminReservationPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 예약 시간 페이지를 조회한다.")
    void adminTimePage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
