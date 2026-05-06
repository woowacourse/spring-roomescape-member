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
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
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
                .statusCode(200);

        assertThat(jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class))
                .isEqualTo(0);
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertReservationTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }
}
