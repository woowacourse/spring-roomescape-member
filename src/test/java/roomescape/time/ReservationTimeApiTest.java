package roomescape.time;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class ReservationTimeApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("사용자는 특정 날짜와 테마의 예약 가능 시간을 조회할 수 있다")
    void findAvailableTimes() {
        createTime("10:00");
        createTime("11:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        RestAssured.given().log().all()
                .queryParam("date", date.toString())
                .queryParam("themeId", themeId)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].startAt", is("10:00:00"))
                .body("[1].startAt", is("11:00:00"));
    }
}
