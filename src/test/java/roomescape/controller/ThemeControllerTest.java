package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {
    @Autowired
    private ThemeController themeController;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_가능한_시간_조회_API() {
        // given
        // 1. 테마 추가
        jdbcTemplate.execute("INSERT INTO theme (name, description, url) VALUES ('우테코 공포물', '레벨2 미션의 공포', '/horror')");

        // 2. 전체 시간대 추가 (10:00, 13:00)
        jdbcTemplate.execute("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.execute("INSERT INTO reservation_time (start_at) VALUES ('13:00:00')");

        // 3. 이미 예약된 내역 추가 (10:00 예약됨)
        jdbcTemplate.update("""
                    INSERT INTO reservation (name, date, time_id, theme_id) 
                    VALUES (?, ?, ?, ?)
                """, "브라운", "2026-05-04", 1, 1);

        // when & then
        RestAssured.given().log().all()
                .when().get("/theme/1/available-time?date=2026-05-04")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("최근 1주동안 예약이 많았던 테마를 조회하는 정상 테스트")
    void 테마_조회_API() {
        // given
        // 1. 테마 추가
        jdbcTemplate.execute("INSERT INTO theme (name, description, url) VALUES ('미래 도시', '2050년 서울의 이야기', '/future-city')");
        jdbcTemplate.execute("INSERT INTO theme (name, description, url) VALUES ('고대 이집트', '파라오의 저주를 풀어라', '/egypt')");
        jdbcTemplate.execute("INSERT INTO theme (name, description, url) VALUES ('마법 학교', '마법사가 되기 위한 여정', '/magic-school')");

        // 2. 전체 시간대 추가
        jdbcTemplate.execute("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.execute("INSERT INTO reservation_time (start_at) VALUES ('11:00:00')");
        jdbcTemplate.execute("INSERT INTO reservation_time (start_at) VALUES ('12:00:00')");

        // 3. 이미 예약된 내역 추가
        String sql = """
                    INSERT INTO reservation (name, date, time_id, theme_id) 
                    VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, "브라운", "2026-05-04", 1, 1);
        jdbcTemplate.update(sql, "브라운", "2026-05-05", 1, 2);

        // when & then
        RestAssured.given().log().all()
                .when().get("/theme?limit=2")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
