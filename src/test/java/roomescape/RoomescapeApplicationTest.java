package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {
    private static final String AVAILABLE_DATE = "2099-06-01";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        jdbcTemplate.update("insert into reservation_time(start_at) values ('10:00')");
        jdbcTemplate.update(
                "insert into theme(name, description, thumbnail_url) values ('공포', '무서워요', 'https://zeze.com')");
        jdbcTemplate.update(
                "insert into theme(name, description, thumbnail_url) values ('개그', '재밌어요', 'https://zeze.com')");
    }

    @Test
    void 예약_생성_후_사용_시간_조회시_해당_시간이_제외된다() {
        int before = availableCount(AVAILABLE_DATE, 1);

        reserve("제제", AVAILABLE_DATE, 1L, 1L);

        int after = availableCount(AVAILABLE_DATE, 1);
        assertThat(after).isEqualTo(before - 1);
    }

    @Test
    void 예약_없는_날짜_조회시_전체_시간이_반환된다() {
        int total = RestAssured.given()
                .when().get("/times")
                .then().statusCode(200)
                .extract().jsonPath().getList(".").size();

        int available = availableCount(AVAILABLE_DATE, 1);

        assertThat(available).isEqualTo(total);
    }

    @Test
    void 다른_테마_예약은_사용_시간_조회에_영향을_주지_않는다() {
        int before = availableCount(AVAILABLE_DATE, 1);

        reserve("제제", AVAILABLE_DATE, 1L, 2L);

        int after = availableCount(AVAILABLE_DATE, 1);
        assertThat(after).isEqualTo(before);
    }

    @Test
    void 과거_날짜로_사용_시간_조회시_400을_반환한다() {
        String past = "2020-01-01";

        RestAssured.given()
                .when().get("/times/available?date=" + past + "&themeId=1")
                .then().statusCode(400);
    }

    @Test
    void themeId_없이_사용_시간_조회시_400을_반환한다() {
        RestAssured.given()
                .when().get("/times/available?date=" + AVAILABLE_DATE)
                .then().statusCode(400);
    }

    @Test
    void date_없이_가용_시간_조회시_400을_반환한다() {
        RestAssured.given()
                .when().get("/times/available?themeId=1")
                .then().statusCode(400);
    }

    private int availableCount(String date, long themeId) {
        return RestAssured.given()
                .when().get("/times/available?date=" + date + "&themeId=" + themeId)
                .then().statusCode(200)
                .extract().jsonPath().getList(".").size();
    }

    private void reserve(String name, String date, Long timeId, Long themeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", date);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().statusCode(201);
    }
}