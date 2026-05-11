package roomescape.acceptancetest.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public final class AcceptanceTestFixture {

    private AcceptanceTestFixture() {
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate reservationDate() {
        return today().plusDays(1);
    }

    public static void createTheme() {
        createTheme("미술관의 밤");
    }

    public static void createTheme(final String name) {
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

    public static void createReservationTime(final String startAt, final Long themeId) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/themes/" + themeId + "/times")
                .then().log().all()
                .statusCode(201);
    }

    public static void createReservation(final String name, final LocalDate date, final Long timeId) {
        createReservation(name, date.toString(), timeId);
    }

    public static void createReservation(final String name, final String date, final Long timeId) {
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

    public static void insertReservation(
            final JdbcTemplate jdbcTemplate,
            final String name,
            final LocalDate date,
            final Long timeId
    ) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                name,
                date,
                timeId
        );
    }

    public static Map<String, Object> reservationRequest(final String name, final LocalDate date, final long timeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date.toString());
        reservation.put("timeId", timeId);
        return reservation;
    }

}
