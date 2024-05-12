package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/delete-data.sql", "/data.sql"})
class RoomEscapeApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    class ReservationTest {

        @LocalServerPort
        private int port;

        @BeforeEach
        void beforeEach() {
            RestAssured.port = this.port;
        }

        @Test
        @DisplayName("예약을 생성한다.")
        void postReservation() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", "2025-08-05");
            params.put("timeId", 1);
            params.put("themeId", 1);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .header("Location", "/reservations/14");
        }

        @Test
        @DisplayName("예약 목록을 조회한다.")
        void getReservations() {
            List<Reservation> reservations = RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(200).extract()
                    .jsonPath().getList(".");

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

            assertThat(reservations.size()).isEqualTo(count);
        }

        @Test
        @DisplayName("예약을 조회한다.")
        void getReservation() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/reservations/3")
                    .then().log().all()
                    .statusCode(200);

        }

        @Test
        @DisplayName("예약 가능한 시간 목록을 조회한다.")
        void getAvailableTimes() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/reservations/times?date=2024-04-23&themeId=1")
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("예약을 삭제한다.")
        void deleteReservation() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/reservations/3")
                    .then().log().all()
                    .statusCode(204);
        }
    }

    @Nested
    class ReservationTimeTest {

        @LocalServerPort
        private int port;

        @BeforeEach
        void beforeEach() {
            RestAssured.port = this.port;
        }

        @Test
        @DisplayName("예약 시간을 생성한다.")
        void postReservationTime() {
            Map<String, Object> params = new HashMap<>();
            params.put("startAt", "08:00");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(201)
                    .header("Location", "/times/7");
        }

        @Test
        @DisplayName("예약 시간 목록을 조회한다.")
        void getReservationsTime() {
            List<ReservationTime> reservations = RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(200).extract()
                    .jsonPath().getList(".");

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

            assertThat(reservations.size()).isEqualTo(count);
        }

        @Test
        @DisplayName("예약 시간을 조회한다.")
        void getReservationTime() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/times/3")
                    .then().log().all()
                    .statusCode(200);

        }

        @Test
        @DisplayName("예약 시간을 삭제한다.")
        void deleteReservationTime() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/times/6")
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("예약 시간이 사용 중인 예약이 존재할 경우, 시간 삭제 요청 시 409를 반환한다.")
        void deleteReservationTime_isConflict() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/times/3")
                    .then().log().all()
                    .statusCode(409);
        }
    }

    @Nested
    class ThemeTest {

        @LocalServerPort
        private int port;

        @BeforeEach
        void beforeEach() {
            RestAssured.port = this.port;
        }

        @Test
        @DisplayName("테마를 생성한다.")
        void postTheme() {
            Map<String, Object> params = new HashMap<>();
            params.put("name", "몰리");
            params.put("description", "드디어");
            params.put("thumbnail", "https://끝");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(201)
                    .header("Location", "/themes/11");
        }

        @Test
        @DisplayName("테마 목록을 조회한다.")
        void getTheme() {
            List<Theme> themes = RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200).extract()
                    .jsonPath().getList(".");

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

            assertThat(themes.size()).isEqualTo(count);
        }

        @Test
        @DisplayName("테마를 조회한다.")
        void getThemes() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/themes/popular")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", Matchers.is(6));
        }

        @Test
        @DisplayName("테마를 삭제한다.")
        void deleteTheme() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/themes/10")
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("존재하지 않는 테마를 삭제 요청 시에도 204를 반환한다.")
        void deleteTheme_NotFound() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/themes/999999999")
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("예약 테마가 사용 중인 예약이 존재할 경우, 테마 삭제 요청 시 409를 반환한다.")
        void deleteTheme_isConflict() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/themes/3")
                    .then().log().all()
                    .statusCode(409);
        }
    }

}
