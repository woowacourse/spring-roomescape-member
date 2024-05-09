package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationApiControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약 생성에 성공하면 201을 반환한다.")
    @Test
    void return_201_when_reservation_create_success() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2024-06-01");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then()
                .statusCode(201);
    }

    @DisplayName("유효하지 않은 예약자명, 날짜로 예약 생성하면 400을 반환한다.")
    @ParameterizedTest
    @MethodSource("invalidCreateInput")
    void return_400_when_reservation_create_input_is_invalid(final String name, final String date) {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date);
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then()
                .statusCode(400);
    }

    private static Stream<Arguments> invalidCreateInput() {
        return Stream.of(
                Arguments.arguments("", "2024-06-01"),
                Arguments.arguments("제리", "")
        );
    }

    @DisplayName("중복된 예약을 생성하면 400를 반환한다.")
    @Test
    void return_400_when_duplicate_reservation() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");

        ReservationFixture.createAndReturnId("2024-06-01", timeId, themeId);

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "조이썬");
        reservation.put("date", "2024-06-01");
        reservation.put("timeId", themeId);
        reservation.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then()
                .statusCode(400);
    }

    @DisplayName("지나간 날짜와 시간으로 예약을 생성하면 400를 반환한다.")
    @Test
    void return_400_when_create_past_time_reservation() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "제리");
        reservation.put("date", "2024-01-02");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then()
                .statusCode(400);
    }

    @DisplayName("예약 조회에 성공하면 200을 반환한다.")
    @Test
    void return_200_when_get_reservations_success() {
        final Long timeId1 = ReservationTimeFixture.createAndReturnId("10:00");
        final Long timeId2 = ReservationTimeFixture.createAndReturnId("11:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");

        ReservationFixture.createAndReturnId("2024-06-01", timeId1, themeId);
        ReservationFixture.createAndReturnId("2024-06-01", timeId2, themeId);

        RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200);
    }

    @DisplayName("예약 삭제에 성공하면 204를 반환한다.")
    @Test
    void return_204_when_reservation_delete_success() {
        final Long timeId = ReservationTimeFixture.createAndReturnId("10:00");
        final Long themeId = ThemeFixture.createAndReturnId("테마 1");
        final Long reservationId = ReservationFixture.createAndReturnId("2024-06-01", timeId, themeId);

        RestAssured.given()
                .delete("/reservations/" + reservationId)
                .then()
                .statusCode(204);
    }

    @DisplayName("특정 예약이 존재하지 않는데, 그 예약을 삭제하려 하면 404을 반환한다.")
    @Test
    void return_404_when_not_exist_id() {
        RestAssured.given()
                .delete("/reservations/-1")
                .then()
                .statusCode(404);
    }
}
