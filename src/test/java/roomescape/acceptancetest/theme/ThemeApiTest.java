package roomescape.acceptancetest.theme;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;

@RoomecapeAcceptanceTest
public class ThemeApiTest {

    @Test
    void 인기_테마_API() {
        createTheme("미술관의 밤");
        createTheme("사라진 열쇠");
        createTheme("비밀의 방");
        createReservationTime("10:00", 1L);
        createReservationTime("11:00", 1L);
        createReservationTime("12:00", 2L);
        createReservationTime("13:00", 3L);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate periodStart = LocalDate.now().minusDays(7);
        createReservation("브라운", yesterday.toString(), 1L);
        createReservation("코니", yesterday.toString(), 2L);
        createReservation("샐리", periodStart.toString(), 3L);
        createReservation("문", LocalDate.now().minusDays(8).toString(), 4L);

        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/theme/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("id", contains(1, 2))
                .body("name", contains("미술관의 밤", "사라진 열쇠"));
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
