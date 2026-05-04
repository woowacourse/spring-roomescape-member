package roomescape.controller;

import io.restassured.RestAssured;
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
}
