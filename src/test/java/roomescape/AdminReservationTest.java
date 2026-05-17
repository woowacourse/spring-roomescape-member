package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 관리자_예약_조회() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 관리자_예약_추가_및_삭제() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class))
                .isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        assertThat(jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class))
                .isEqualTo(0);
    }

    @Test
    void 관리자_예약_단건_조회() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);

        RestAssured.given().log().all()
                .when().get("/admin/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("date", is("2030-08-05"));
    }


    private void insertUser(Long id, String name, String email) {
        jdbcTemplate.update("INSERT INTO users(id, name, email) VALUES (?, ?, ?)", id, name, email);
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertReservationTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(Long userId, Long themeId, String date, Long timeId) {
        jdbcTemplate.update("INSERT INTO reservation(user_id, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                userId, themeId, date, timeId);
    }
}
