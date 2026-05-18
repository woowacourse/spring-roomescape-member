package roomescape.acceptance;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void DELETE_admin_reservations_id_예약을_삭제한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void DELETE_admin_reservations_id_없는_id면_404과_메시지를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/reservations/9999")
                .then().log().all()
                .statusCode(404)
                .body("message", equalTo("예약을(를) 찾을 수 없습니다. id=9999"));
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