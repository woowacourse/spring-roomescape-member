package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마_전체_목록_반환() {
        insertTheme();
        insertTheme();

        List<Map<String, Object>> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(themes).hasSize(2);
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
        jdbcTemplate.update("INSERT INTO users(name, email) VALUES (?, ?)", "홍길동", "hong@test.com");
        jdbcTemplate.update(
                "INSERT INTO reservation(user_id, theme_id, date, time_id) VALUES (1, 1, '2026-05-06', 1)");

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

        Boolean isReserved = RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getBoolean("[0].isReserved");

        assertThat(isReserved).isFalse();
    }

    @Test
    @Sql("classpath:data.sql")
    void 인기_테마_상위_10개를_예약_수_내림차순으로_반환() {
        List<Map<String, Object>> themes = RestAssured.given().log().all()
                .when().get("/themes/popular?limit=10")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".");

        assertThat(themes).hasSize(10);

        // 1~6위는 예약 수가 달라 순위 고정
        assertThat(themes.get(0)).containsEntry("id", 1).containsEntry("reservedCount", 10);
        assertThat(themes.get(1)).containsEntry("id", 2).containsEntry("reservedCount", 9);
        assertThat(themes.get(2)).containsEntry("id", 3).containsEntry("reservedCount", 8);
        assertThat(themes.get(3)).containsEntry("id", 4).containsEntry("reservedCount", 7);
        assertThat(themes.get(4)).containsEntry("id", 5).containsEntry("reservedCount", 6);
        assertThat(themes.get(5)).containsEntry("id", 6).containsEntry("reservedCount", 5);

        // 7~8위는 동률(4건), id 오름차순으로 정렬
        assertThat(themes.get(6)).containsEntry("id", 7).containsEntry("reservedCount", 4);
        assertThat(themes.get(7)).containsEntry("id", 8).containsEntry("reservedCount", 4);

        // 9~10위는 동률(3건), id 오름차순으로 정렬
        assertThat(themes.get(8)).containsEntry("id", 9).containsEntry("reservedCount", 3);
        assertThat(themes.get(9)).containsEntry("id", 10).containsEntry("reservedCount", 3);
    }

    @Test
    @Sql("classpath:data.sql")
    void 인기_테마_조회_시_기간_외_예약은_집계에서_제외() {
        // 테마 13~15는 기간 외 예약이므로 예약 상위 10개에 포함되지 않아야 함
        List<Integer> themeIds = RestAssured.given().log().all()
                .when().get("/themes/popular?limit=10")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList("id");

        assertThat(themeIds).doesNotContain(13, 14, 15);
    }

    private void insertTheme() {
        jdbcTemplate.execute(
                "INSERT INTO theme(name, description, thumbnail_image_url) VALUES ('테마명', '테마설명', 'https://thumbnail.url')");
    }
}
