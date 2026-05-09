package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.Date;
import java.sql.Time;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;


/*
 * 미션2 사이클1 - 인기 테마 조회 API 요구사항 테스트.
 * IntegrationTest 상속으로 매 테스트 빈 DB 보장.
 * 시간 의존성을 풀기 위해 @TestConfiguration으로 고정 Clock을 주입.
 * 각 테스트가 @BeforeEach에서 자기 데이터(테마 + 예약)를 직접 준비.
 *
 * 비즈니스 규칙:
 *  1) 7일 이내 예약만 집계 (8일 이전은 제외)
 *  2) 오늘 예약은 제외
 *  3) 예약 건수 내림차순 정렬
 *  4) 최대 10개
 */
public class PopularThemeStepTest extends IntegrationTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 5, 9);

    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(
                    TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long timeId;
    private Long islandThemeId;
    private Long cityThemeId;
    private Long balloonThemeId;

    @BeforeEach
    void setUp() {
        // 모든 테스트 공통: 시간 1개, 핵심 테마 3개
        timeId = insertTime(LocalTime.of(10, 0));
        islandThemeId = insertTheme("무인도 탈출", "...", "https://example.com/island.jpg");
        cityThemeId = insertTheme("도시 탈출", "...", "https://example.com/city.jpg");
        balloonThemeId = insertTheme("열기구 탈출", "...", "https://example.com/balloon.jpg");

        LocalDate yesterday = TODAY.minusDays(1);
        LocalDate fiveDaysAgo = TODAY.minusDays(5);
        LocalDate eightDaysAgo = TODAY.minusDays(8);

        // 무인도: 어제 3건 + 5일 전 2건 = 5건 (1등)
        insertReservation("user1", yesterday, timeId, islandThemeId);
        insertReservation("user2", yesterday, timeId, islandThemeId);
        insertReservation("user3", yesterday, timeId, islandThemeId);
        insertReservation("user4", fiveDaysAgo, timeId, islandThemeId);
        insertReservation("user5", fiveDaysAgo, timeId, islandThemeId);

        // 도시: 5일 전 4건 + 8일 전 2건 = 집계상 4건 (2등)
        insertReservation("user6", fiveDaysAgo, timeId, cityThemeId);
        insertReservation("user7", fiveDaysAgo, timeId, cityThemeId);
        insertReservation("user8", fiveDaysAgo, timeId, cityThemeId);
        insertReservation("user9", fiveDaysAgo, timeId, cityThemeId);
        insertReservation("user10", eightDaysAgo, timeId, cityThemeId);
        insertReservation("user11", eightDaysAgo, timeId, cityThemeId);

        // 열기구: 어제 1건
        insertReservation("user12", yesterday, timeId, balloonThemeId);

        // 무인도 오늘 5건 (오늘이라 집계 제외 검증용)
        insertReservation("user13", TODAY, timeId, islandThemeId);
        insertReservation("user14", TODAY, timeId, islandThemeId);
        insertReservation("user15", TODAY, timeId, islandThemeId);
        insertReservation("user16", TODAY, timeId, islandThemeId);
        insertReservation("user17", TODAY, timeId, islandThemeId);
    }


    @Test
    @DisplayName("예약 건수 내림차순으로 정렬된다")
    void 예약_건수_내림차순_정렬() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        // 1등은 무인도(5건), 2등은 도시(4건)
        assert names.get(0).equals("무인도 탈출") : "1등은 무인도여야 함, 실제: " + names.get(0);
        assert counts.get(0) == 5 : "1등 건수는 5여야 함, 실제: " + counts.get(0);
        assert names.get(1).equals("도시 탈출") : "2등은 도시여야 함, 실제: " + names.get(1);
        assert counts.get(1) == 4 : "2등 건수는 4여야 함, 실제: " + counts.get(1);
    }

    @Test
    @DisplayName("8일 전 예약은 집계에서 제외된다")
    void 기간_밖_예약_제외() {
        // 도시 테마는 5일전 4건 + 8일전 2건 = 총 6건 예약이지만,
        // 8일전이 제외되면 4건이 집계되어야 함
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/user/themes/popular")
                .then().statusCode(200).extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        int cityIndex = names.indexOf("도시 탈출");
        assert cityIndex >= 0 : "도시 테마가 응답에 있어야 함";
        assert counts.get(cityIndex) == 4
                : "도시 테마 건수는 4여야 함 (8일전 2건 제외), 실제: " + counts.get(cityIndex);
    }

    @Test
    @DisplayName("오늘 예약은 집계에서 제외된다")
    void 오늘_예약_제외() {
        // 무인도 테마는 어제 3 + 5일전 2 + 오늘 5 = 총 10건이지만,
        // 오늘이 제외되면 5건만 집계되어야 함
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/user/themes/popular")
                .then().statusCode(200).extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        int islandIndex = names.indexOf("무인도 탈출");
        assert islandIndex >= 0 : "무인도 테마가 응답에 있어야 함";
        assert counts.get(islandIndex) == 5
                : "무인도 테마 건수는 5여야 함 (오늘 5건 제외), 실제: " + counts.get(islandIndex);
    }

    @Test
    @DisplayName("최대 10개를 반환한다")
    void 최대_10개를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(10));
    }

    @Test
    @DisplayName("응답 항목은 테마 정보와 예약 건수를 포함한다")
    void 응답_항목_형태() {
        RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("[0].name", is("무인도 탈출"))
                .body("[0].reservationCount", is(5));
    }

    private Long insertTime(LocalTime startAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(startAt));
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class, Time.valueOf(startAt));
    }

    private Long insertTheme(String name, String description, String thumbnailUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                name, description, thumbnailUrl);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class, name);
    }

    private void insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, Date.valueOf(date), timeId, themeId);
    }

}
