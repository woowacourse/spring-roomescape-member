package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마_추가() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마명");
        params.put("description", "테스트 테마입니다.");
        params.put("thumbnailImageUrl", "http://www.test.com/testImageUrl");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_삭제() {
        insertTheme();

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 테마_DB_추가() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "테마명");
        params.put("description", "테스트 테마입니다.");
        params.put("thumbnailImageUrl", "http://www.test.com/testImageUrl");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 테마_DB_삭제() {
        insertTheme();

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);
        assertThat(count).isEqualTo(0);
    }


    private void insertTheme() {
        jdbcTemplate.execute(
                "INSERT INTO theme(name, description, thumbnail_image_url) VALUES ('테마명', '테마설명', 'https://thumbnail.url')");
    }

    @Test
    void 테마_예약_시간_전체_목록_반환() {
        insertTheme();
        jdbcTemplate.execute("INSERT INTO reservation_time(start_at) VALUES ('10:00:00')");
        jdbcTemplate.execute("INSERT INTO reservation_time(start_at) VALUES ('12:00:00')");

        List<Map<String, Object>> times = RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(times).hasSize(2);
    }

    @Test
    void 예약된_시간은_isReserved_true() {
        insertTheme();
        jdbcTemplate.execute("INSERT INTO reservation_time(start_at) VALUES ('10:00:00')");
        jdbcTemplate.execute(
                "INSERT INTO reservation(name, theme_id, date, time_id) VALUES ('홍길동', 1, '2026-05-06', 1)");

        Boolean isReserved = RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getBoolean("[0].isReserved");

        assertThat(isReserved).isTrue();
    }

    @Test
    void 예약되지_않은_시간은_isReserved_false() {
        insertTheme();
        jdbcTemplate.execute("INSERT INTO reservation_time(start_at) VALUES ('10:00:00')");
        // 예약 없이 시간만 존재

        Boolean isReserved = RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getBoolean("[0].isReserved");

        assertThat(isReserved).isFalse();
    }
}
