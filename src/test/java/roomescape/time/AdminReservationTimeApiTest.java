package roomescape.time;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class AdminReservationTimeApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("관리자는 예약 시간을 생성할 수 있다")
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", is("10:00:00"));
    }

    @Test
    @DisplayName("관리자는 예약 시간 목록을 조회할 수 있다")
    void findReservationTimes() {
        createTime("10:00");

        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].startAt", is("10:00:00"));
    }

    @Test
    @DisplayName("관리자는 예약 시간을 삭제할 수 있다")
    void deleteReservationTime() {
        Long timeId = createTime("10:00");

        RestAssured.given().log().all()
                .when().delete("/admin/times/{id}", timeId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다")
    void cannotDeleteReservedTime() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .when().delete("/admin/times/{id}", timeId)
                .then().log().all()
                .statusCode(409)
                .body("message", is("예약이 있는 예약 시간은 삭제할 수 없습니다."));
    }
}
