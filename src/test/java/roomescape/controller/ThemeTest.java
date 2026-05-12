package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
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
class ThemeTest {

    @Nested
    class 데이터없는테스트 {

        @Test
        void 테마_관리_API() {
            Map<String, String> params = new HashMap<>();
            params.put("name", "무서운 이야기");
            params.put("description", "공포");
            params.put("url", "http://example.com");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/admin/themes")
                    .then().log().all()
                    .statusCode(201);

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));

            RestAssured.given().log().all()
                    .when().delete("/admin/themes/1")
                    .then().log().all()
                    .statusCode(204);
        }
    }

    @Nested
    class 데이터있는테스트 {

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
        void 인기_테마_조회_order_by() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));  // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));   // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 2, 2)", now.minusDays(11));  // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));  // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));   // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));  // 7일 범위 안

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", contains(5, 3));
        }

        @Test
        void 인기_테마_조회_예약_개수_동일() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));  // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));   // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(3));   // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));  // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));   // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));  // 7일 범위 안

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", contains(3, 5));
        }

        @Test
        void 인기_테마_조회_예약이_있는_테마() {
            LocalDate now = LocalDate.now();

            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', ?, 1, 1)", now.minusDays(9));  // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('네오', ?, 2, 3)", now.minusDays(10));   // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 2, 2)", now.minusDays(11));  // 7일 범위 밖
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('제이슨', ?, 3, 2)", now.minusDays(6));  // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('워니', ?, 5, 4)", now.minusDays(5));   // 7일 범위 안
            jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('포비', ?, 5, 1)", now.minusDays(4));  // 7일 범위 안

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(2));
        }
    }
}
