package roomescape.acceptancetest.reservationtime;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;

@RoomecapeAcceptanceTest
public class ReservationTimeApiTest {

    @Test
    void 예약_가능_시간_API() {
        createTheme("미술관의 밤");
        createReservationTime("10:00", 1L);
        createReservationTime("11:00", 1L);

        String date = String.valueOf(LocalDate.now());
        createReservation("브라운", date, 1L);

        RestAssured.given().log().all()
                .queryParam("date", date)
                .when().get("/themes/1/available-times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("id", contains(2))
                .body("startAt", contains("11:00:00"));
    }

    private void createTheme(final String name) {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", name);
        theme.put("description", "추리 테마");
        theme.put("thumbnailUrl", "https://example.com/theme.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservationTime(final String startAt, final Long themeId) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/themes/" + themeId + "/times")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservation(final String name, final String date, final Long timeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date);
        reservation.put("timeId", timeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

}
