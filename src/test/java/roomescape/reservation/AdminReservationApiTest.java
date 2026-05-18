package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class AdminReservationApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("관리자는 전체 예약 목록을 조회할 수 있다")
    void findAllReservations() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is(date.toString()))
                .body("[0].time", is("10:00:00"))
                .body("[0].theme.name", is("테마1"));
    }

    @Test
    @DisplayName("관리자는 예약을 생성할 수 있다")
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
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", is("브라운"))
                .body("date", is(date.toString()))
                .body("time", is("10:00:00"))
                .body("theme.name", is("테마1"))
                .body("status", is("RESERVED"));
    }

    @Test
    @DisplayName("관리자는 예약을 취소할 수 있다")
    void cancelReservation() {
        Long timeId = createTime("10:00");
        Long themeId = createActiveTheme("테마1");
        LocalDate date = LocalDate.now().plusDays(1);

        Long reservationId = createReservation("브라운", date, timeId, themeId);

        RestAssured.given().log().all()
                .when().patch("/admin/reservations/{id}/cancel", reservationId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(reservationId.intValue()))
                .body("status", is("CANCELED"));
    }
}
