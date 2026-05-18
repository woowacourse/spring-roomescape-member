package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ThemeControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean(enforceOverride = true)
    Clock clock;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpClock() {
        RestAssured.port = port;
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
        given(clock.instant()).willReturn(Instant.parse("2026-05-06T14:00:00Z"));
    }

    @Test
    void 테마_저장_API_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운게 딱 좋아");
        params.put("description", "무서운 분위기의 방탈출");
        params.put("thumbnailUrl", "https://example.com/theme.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_추가_및_삭제_API_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "무서운게 딱 좋아");
        params.put("description", "무서운 분위기의 방탈출");
        params.put("thumbnailUrl", "https://example.com/theme.jpg");

        // CASCADE 해결용
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM reservation");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 각_날짜에_존재하는_모든_테마_조회_API_테스트() {
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-05")
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }

    @Test
    void 최근_7일_예약_개수에_따른_인기_테마_조회_API_테스트() {
        RestAssured.given().log().all()
                .queryParam("day", 7)
                .queryParam("limit", 10)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].id", is(3))
                .body("[1].id", is(2))
                .body("[2].id", is(1));
    }

    @Test
    void 테마_전체_조회_API_테스트(){
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].id", is(1))
                .body("[1].id", is(2))
                .body("[2].id", is(3))
                .body("[3].id", is(4))
                .body("[4].id", is(5))
                .body("[5].id", is(6))
                .body("[6].id", is(7))
                .body("[7].id", is(8))
                .body("[8].id", is(9))
                .body("[9].id", is(10));
    }
}
