package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("테마를 생성할 수 있다.")
    void 테마_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운 이야기");
        params.put("description", "공포");
        params.put("url", "http://example.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("data.name", is("무서운 이야기"))
                .body("data.description", is("공포"))
                .body("data.url", is("http://example.com"));
    }

    @Test
    @DisplayName("테마가 없으면 목록 조회 시 빈 결과를 반환한다.")
    void 테마_없을때_목록_조회() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(0));
    }

    @Test
    @DisplayName("예약이 없으면 인기 테마 조회 시 빈 결과를 반환한다.")
    void 인기_테마_없을때_조회() {
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(0));
    }

    @Test
    @DisplayName("테마 목록을 조회할 수 있다.")
    void 테마_목록_조회() {
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('무서운 이야기', '공포', 'http://example.com')");
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('해적왕의 보물', '모험', 'http://example.com/2')");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(2));
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void 테마_삭제_성공() {
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('무서운 이야기', '공포', 'http://example.com')");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("이름이 없는 경우 테마를 생성할 수 없다.")
    void 테마_생성_실패_이름_없음() {
        Map<String, String> params = new HashMap<>();
        params.put("description", "공포");
        params.put("url", "http://example.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 테마인 경우 삭제할 수 없다.")
    void 테마_삭제_실패_존재하지_않는_테마() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/999")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약이 있는 테마인 경우 삭제할 수 없다.")
    void 테마_삭제_실패_예약이_있는_테마() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('무서운 이야기', '공포', 'http://example.com')");
        jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', '2026-08-04', 1, 1)");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("ok", is(false));
    }

    @Nested
    class 인기_테마_조회 {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @BeforeEach
        void setUp() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:00')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('13:00')");

            jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('인형의 집', '공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.', 'https://example.com/1')");
            jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('해적왕의 보물', '침몰한 해적선 속 숨겨진 황금 보물을 찾아 제한 시간 내에 탈출해야 합니다.', 'https://example.com/2')");
            jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('명탐정의 부재', '사라진 명탐정의 사무실에 남겨진 단서들을 조합해 진범을 밝혀내세요.', 'https://example.com/3')");
            jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('우주정거장: 블랙아웃', '산소가 고갈되기 직전의 우주정거장에서 탈출 포드를 가동하세요.', 'https://example.com/4')");
            jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('꿈속의 과자집', '꿈속에서 길을 잃은 당신, 달콤하지만 위험한 과자집의 비밀을 풀어야 합니다.', 'https://example.com/5')");
        }

        @Test
        @DisplayName("7일 범위 내 예약 수 기준으로 내림차순 정렬하여 인기 테마를 조회할 수 있다.")
        void 인기_테마_조회_예약_수_내림차순() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 2, 2)", now.minusDays(11));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("data.id", contains(5, 3));
        }

        @Test
        @DisplayName("7일 범위 내 예약 수가 동일한 경우 테마 id 오름차순으로 조회할 수 있다.")
        void 인기_테마_조회_예약_수_동일시_id_오름차순() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(3));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("data.id", contains(3, 5));
        }

        @Test
        @DisplayName("7일 범위 밖 예약인 경우 인기 테마 조회에 포함되지 않는다.")
        void 인기_테마_조회_범위_밖_예약_제외() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 2, 2)", now.minusDays(11));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("data.size()", is(2));
        }
    }
}
