package roomescape;

import static org.hamcrest.Matchers.is;

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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * 미션2 사이클2 - 내 예약 관리 API 통합 테스트.
 * 조회/취소/변경을 본인 검증과 함께 검증한다.
 */
public class MyReservationStepTest extends IntegrationTest {

    private static final LocalDate FUTURE_DATE_1 = LocalDate.of(2050, 5, 15);
    private static final LocalDate FUTURE_DATE_2 = LocalDate.of(2050, 5, 20);

    private static final LocalDate TODAY = LocalDate.of(2026, 5, 13);
    private static final LocalTime NOW_TIME = LocalTime.of(12, 0);

    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(
                    TODAY.atTime(NOW_TIME).atZone(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long timeId10;
    private Long timeId11;
    private Long themeId;

    @BeforeEach
    void setUp() {
        timeId10 = insertTime(LocalTime.of(10, 0));
        timeId11 = insertTime(LocalTime.of(11, 0));
        themeId = insertTheme("테마A", "설명", "https://example.com/a.jpg");
    }

    @Nested
    @DisplayName("내 예약 조회")
    class MyReservationList {

        @Test
        @DisplayName("내 이름으로 된 예약 목록을 날짜, 시간 순으로 반환한다")
        void 내_예약_조회() {
            // 브라운 다른 날짜로 2개 예약
            insertReservation("브라운", FUTURE_DATE_2, timeId10, themeId);
            insertReservation("브라운", FUTURE_DATE_1, timeId11, themeId);
            // 다른 사람의 예약 1개 (필터링 검증용)
            insertReservation("콘", FUTURE_DATE_1, timeId10, themeId);

            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when().get("/user/reservations?name=브라운")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(2))
                    .extract();

            List<String> dates = response.jsonPath().getList("date");
            assert dates.get(0).equals(FUTURE_DATE_1.toString())
                    : "첫 항목은 더 가까운 날짜여야 함, 실제: " + dates.get(0);
            assert dates.get(1).equals(FUTURE_DATE_2.toString());
        }

        @Test
        @DisplayName("같은 날짜에 여러 예약이 있으면 시간 순으로 정렬된다")
        void 같은_날짜_시간_정렬() {
            insertReservation("브라운", FUTURE_DATE_1, timeId11, themeId);
            insertReservation("브라운", FUTURE_DATE_1, timeId10, themeId);

            ExtractableResponse<Response> response = RestAssured.given()
                    .when().get("/user/reservations?name=브라운")
                    .then().statusCode(200).extract();

            List<String> times = response.jsonPath().getList("time.startAt");
            assert times.get(0).equals("10:00") : "첫 항목은 더 이른 시간이어야 함";
            assert times.get(1).equals("11:00");
        }

        @Test
        @DisplayName("해당 이름의 예약이 없으면 빈 배열을 반환한다")
        void 예약_없으면_빈_배열() {
            RestAssured.given().log().all()
                    .when().get("/user/reservations?name=존재하지않는사람")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        @DisplayName("name 파라미터가 누락되면 400을 반환한다")
        void name_누락() {
            RestAssured.given().log().all()
                    .when().get("/user/reservations")
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is("필수 요청 파라미터가 누락되었습니다."));
        }
    }

    @Nested
    @DisplayName("내 예약 취소")
    class MyReservationCancel {

        @Test
        @DisplayName("본인의 미래 예약을 취소하면 204를 반환하고 실제로 삭제된다")
        void 본인_미래_예약_취소() {
            Long reservationId = insertReservationAndReturnId("브라운", FUTURE_DATE_1, timeId10, themeId);

            RestAssured.given().log().all()
                    .when().delete("/user/reservations/" + reservationId + "?name=브라운")
                    .then().log().all()
                    .statusCode(204);

            RestAssured.given()
                    .when().get("/user/reservations?name=브라운")
                    .then().statusCode(200)
                    .body("size()", is(0));
        }

        @Test
        @DisplayName("존재하지 않는 예약 ID로 취소 시도 → 404")
        void 존재하지_않는_예약() {
            RestAssured.given().log().all()
                    .when().delete("/user/reservations/9999?name=브라운")
                    .then().log().all()
                    .statusCode(404)
                    .body("message", is("존재하지 않는 예약입니다."));
        }

        @Test
        @DisplayName("다른 사람의 예약 취소 시도 → 404 (정보 노출 방지)")
        void 다른_사람의_예약() {
            Long reservationId = insertReservationAndReturnId("브라운", FUTURE_DATE_1, timeId10, themeId);

            RestAssured.given().log().all()
                    .when().delete("/user/reservations/" + reservationId + "?name=콘")
                    .then().log().all()
                    .statusCode(404)
                    .body("message", is("존재하지 않는 예약입니다."));
        }

        @Test
        @DisplayName("이미 지난 예약 취소 시도 → 400")
        void 이미_지난_예약() {
            // 고정 Clock 기준 어제 (2026-05-12)
            LocalDate yesterday = TODAY.minusDays(1);
            Long reservationId = insertReservationAndReturnId("브라운", yesterday, timeId10, themeId);

            RestAssured.given().log().all()
                    .when().delete("/user/reservations/" + reservationId + "?name=브라운")
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is("이미 지난 예약은 취소할 수 없습니다."));
        }

        @Test
        @DisplayName("name 파라미터 누락 → 400")
        void name_누락() {
            Long reservationId = insertReservationAndReturnId("브라운", FUTURE_DATE_1, timeId10, themeId);

            RestAssured.given().log().all()
                    .when().delete("/user/reservations/" + reservationId)
                    .then().log().all()
                    .statusCode(400)
                    .body("message", is("필수 요청 파라미터가 누락되었습니다."));
        }
    }

    private Long insertReservationAndReturnId(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, Date.valueOf(date), timeId, themeId);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation WHERE name = ? AND date = ? AND time_id = ? AND theme_id = ?",
                Long.class, name, Date.valueOf(date), timeId, themeId);
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
