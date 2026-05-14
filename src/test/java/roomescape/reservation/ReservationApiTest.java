package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class ReservationApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("사용자는 예약을 생성할 수 있다")
    void createReservation() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

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
                .statusCode(201)
                .body("name", is("브라운"))
                .body("date", is(date.toString()))
                .body("time", is("10:00:00"))
                .body("theme.name", is("테마1"))
                .body("status", is("RESERVED"));
    }

    @Test
    @DisplayName("사용자는 이름으로 본인의 예약 목록을 조회할 수 있다")
    void findMyReservations() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .queryParam("name", "브라운")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is(date.toString()))
                .body("[0].time", is("10:00:00"))
                .body("[0].theme.name", is("테마1"))
                .body("[0].status", is("RESERVED"));
    }

    @Test
    @DisplayName("사용자는 본인의 예약 날짜와 시간을 변경할 수 있다")
    void updateReservation() {
        Long firstTimeId = createTime("10:00");
        Long secondTimeId = createTime("11:00");
        Long themeId = createActiveTheme("테마1");

        LocalDate beforeDate = LocalDate.now().plusDays(1);
        LocalDate afterDate = LocalDate.now().plusDays(2);

        Long reservationId = createReservation("브라운", beforeDate, firstTimeId, themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "date", afterDate.toString(),
                        "timeId", secondTimeId
                ))
                .when().patch("/reservations/{id}", reservationId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(reservationId.intValue()))
                .body("date", is(afterDate.toString()))
                .body("time", is("11:00:00"))
                .body("status", is("RESERVED"));
    }

    @Test
    @DisplayName("사용자는 본인의 예약을 취소할 수 있다")
    void cancelReservation() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        Long reservationId = createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .when().patch("/reservations/{id}/cancel", reservationId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(reservationId.intValue()))
                .body("status", is("CANCELED"));
    }

    @Test
    @DisplayName("유효하지 않은 입력값이면 예약을 생성할 수 없다")
    void cannotCreateReservationWithInvalidInput() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "",
                        "date", date.toString(),
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약자 이름은 필수 항목입니다."));
    }

    @Test
    @DisplayName("존재하지 않는 예약은 변경할 수 없다")
    void cannotUpdateNotFoundReservation() {
        Long timeId = createTime("10:00");
        LocalDate date = LocalDate.now().plusDays(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "date", date.toString(),
                        "timeId", timeId
                ))
                .when().patch("/reservations/{id}", 999L)
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약입니다."));
    }

    @Test
    @DisplayName("존재하지 않는 예약은 취소할 수 없다")
    void cannotCancelNotFoundReservation() {
        RestAssured.given().log().all()
                .when().patch("/reservations/{id}/cancel", 999L)
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약입니다."));
    }
}
