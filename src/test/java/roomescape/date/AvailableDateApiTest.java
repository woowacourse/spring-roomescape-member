package roomescape.date;

import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class AvailableDateApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("사용자는 예약 가능한 날짜 목록을 조회할 수 있다")
    void findAvailableDates() {
        RestAssured.given().log().all()
                .when().get("/available-dates")
                .then().log().all()
                .statusCode(200)
                .body("$", notNullValue());
    }
}
