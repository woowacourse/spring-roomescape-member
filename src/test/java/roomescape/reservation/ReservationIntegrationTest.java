package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class ReservationIntegrationTest extends IntegrationTestSupport {

    @Test
    @DisplayName("지나간 날짜와 시간으로 예약을 생성할 수 없다")
    void cannotCreatePastReservation() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().minusDays(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "브라운",
                        "date", date.toString(),
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("과거 날짜/시간으로는 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 중복 예약을 생성할 수 없다")
    void cannotCreateDuplicateReservation() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", date.toString(),
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("message", is("해당 날짜/시간/테마는 이미 예약되었습니다."));
    }

    @Test
    @DisplayName("이미 예약된 날짜와 시간으로 예약을 변경할 수 없다")
    void cannotUpdateToReservedTime() {
        Long firstTimeId = createTime("10:00");
        Long secondTimeId = createTime("11:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        Long reservationId = createReservation("브라운", date, firstTimeId, themeId);
        createReservation("포비", date, secondTimeId, themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "date", date.toString(),
                        "timeId", secondTimeId
                ))
                .when().patch("/reservations/{id}", reservationId)
                .then().log().all()
                .statusCode(409)
                .body("message", is("해당 날짜/시간/테마는 이미 예약되었습니다."));
    }

    @Test
    @DisplayName("지난 예약은 변경할 수 없다")
    void cannotUpdatePastReservation() {
        createTime("10:00");
        Long secondTimeId = createTime("11:00");
        Long themeId = createActiveTheme("테마1");

        LocalDate pastDate = LocalDate.now().minusDays(1);
        Long reservationId = savePastReservation("브라운", pastDate, "10:00", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "date", LocalDate.now().plusDays(1).toString(),
                        "timeId", secondTimeId
                ))
                .when().patch("/reservations/{id}", reservationId)
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지난 예약은 변경할 수 없습니다."));
    }

    @Test
    @DisplayName("지난 예약은 취소할 수 없다")
    void cannotCancelPastReservation() {
        createTime("10:00");
        Long themeId = createActiveTheme("테마1");

        LocalDate pastDate = LocalDate.now().minusDays(1);
        Long reservationId = savePastReservation("브라운", pastDate, "10:00", themeId);

        RestAssured.given().log().all()
                .when().patch("/reservations/{id}/cancel", reservationId)
                .then().log().all()
                .statusCode(400)
                .body("message", is("이미 지난 예약은 취소할 수 없습니다."));
    }
}
