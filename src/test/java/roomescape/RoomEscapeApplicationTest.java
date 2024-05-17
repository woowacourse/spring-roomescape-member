package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;
import roomescape.util.JwtTokenHelper;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/delete-data.sql", "/data.sql"})
class RoomEscapeApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성")
    void createReservationRequest() {
        final String token = jwtTokenHelper.createToken(new Member(1L, "name", "email", "pw", Role.ADMIN));
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2125-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", 1);


        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", token)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/4");
    }

    @Nested
    class LoginTest {

            @Test
            @DisplayName("로그인 한다.")
            void postLogin() {
                Map<String, Object> params = new HashMap<>();
                params.put("email", "email1@woowa.com");
                params.put("password", "password");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/login")
                        .then().log().all()
                        .statusCode(200)
                        .cookie("token");
            }

            @Test
            @DisplayName("틀린 이메일와 패스워드로 로그인을 시도하면 실패한다.")
            void postLogin_InvalidEmailAndPassword() {
                Map<String, Object> params = new HashMap<>();
                params.put("email", "none@woowa.com");
                params.put("password", "none");

                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/login")
                        .then().log().all()
                        .statusCode(401);
            }

            @Test
            @DisplayName("로그인한 사용자의 정보를 조회한다.")
            void getAuthInfo() {
                final String token = jwtTokenHelper.createToken(new Member(1L, "마크", "email", "pw", Role.USER));

                RestAssured.given().log().all()
                        .cookie("token", token)
                        .when().get("/login/check")
                        .then().log().all()
                        .statusCode(200)
                        .body("name", Matchers.is("마크"));
            }

            @Test
            @DisplayName("로그아웃 하면 token 쿠키가 삭제된다.")
            void postLogout() {
                RestAssured.given().log().all()
                        .when().post("/logout")
                        .then().log().all()
                        .statusCode(200)
                        .cookie("token", "");
            }
    }

    @Nested
    class ReservationTest {

        @Test
        @DisplayName("쿠키를 이용해 예약을 생성한다.")
        void postReservation() {
            final String token = jwtTokenHelper.createToken(new Member(1L, "name", "email", "pw", Role.USER));

            Map<String, Object> params = new HashMap<>();
            params.put("date", "2025-08-05");
            params.put("timeId", 1);
            params.put("themeId", 1);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .cookie("token", token)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .header("Location", "/reservations/4");
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
                    .body("size()", Matchers.is(3));
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
