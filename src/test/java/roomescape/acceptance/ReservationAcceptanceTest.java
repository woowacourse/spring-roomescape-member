package roomescape.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void GET_reservations_목록을_조회한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1))
                .body("reservations[0].name", equalTo("브라운"));
    }

    @Test
    void GET_reservations_id_단건을_조회한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("브라운"));
    }

    @Test
    void POST_reservations_예약을_생성한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", matchesPattern("/reservations/\\d+"));
    }

    @Test
    void POST_reservations_같은_날짜시간테마_중복이면_400과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("기존", 1L, "2026-05-06", 1L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("해당 날짜·시간·테마에 이미 예약이 존재합니다."));
    }

    @Test
    void DELETE_reservations_id_예약을_삭제한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(String name, Long themeId, String date, Long timeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                name, themeId, date, timeId);
    }
}