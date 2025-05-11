package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.common.BaseTest;
import roomescape.presentation.dto.response.ReservationResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest extends BaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, Object> reservation;
    private Map<String, String> reservationTime;
    private Map<String, String> theme;
    private Map<String, String> member;
    private Map<String, Object> auth;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        setUpTime();
        setUpTheme();
        setUpMemberAndLogin();
        setUpReservation();
    }

    private void setUpTime() {
        reservationTime = new HashMap<>();
        reservationTime.put("startAt", "10:00");
    }

    private void setUpTheme() {
        theme = new HashMap<>();
        theme.put("name", "테마1");
        theme.put("description", "설명1");
        theme.put("thumbnail", "썸네일1");
    }

    private void setUpMemberAndLogin() {
        member = new HashMap<>();
        member.put("name", "브라운");
        member.put("email", "test@email.com");
        member.put("password", "pass1");

        auth = new HashMap<>();
        auth.put("email", "test@email.com");
        auth.put("password", "pass1");
    }

    private void setUpReservation() {
        reservation = new HashMap<>();
        reservation.put("date", "2025-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);
    }

    @Nested
    class Page {

        @Test
        void 메인_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 관리자_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 방탈출_예약관리_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservation")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 방탈출_시간관리_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/time")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 방탈출_테마관리_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/theme")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 방탈출_사용자_예약페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/reservation")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void 방탈출_로그인_페이지를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/login")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    @Nested
    class Reservation {

        @Test
        void 방탈출_예약을_생성_조회_삭제한다() {
            givenCreatedReservationTime();
            givenCreatedTheme();
            givenCreatedMember();
            String token = givenLoginToken();

            // 생성
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", token)
                    .body(reservation)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            // 조회
            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1));

            // 삭제
            RestAssured.given().log().all()
                    .when().delete("/reservations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(0));
        }

        @Test
        void 방탈출_예약_목록을_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(0));
        }

        @Test
        void 예약_삭제시_존재하지_않는_예약이면_예외를_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/reservations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class ReservationTime {

        @Test
        void 예약_시간을_생성_조회_삭제한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(reservationTime)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            RestAssured.given().log().all()
                    .when().get("/times")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1));

            RestAssured.given().log().all()
                    .when().delete("/times/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 예약_시간_삭제시_존재하지_않는_예약시간이면_예외를_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/times/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void 예약_시간_삭제시_이미_예약이_존재하면_예외를_응답한다() {
            givenCreatedReservationTime();
            givenCreatedTheme();
            givenCreatedMember();
            givenCreatedReservation();

            RestAssured.given().log().all()
                    .when().delete("/times/1")
                    .then().log().all()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    class Theme {

        @Test
        void 테마를_생성_조회_삭제한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(theme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1));

            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 테마_삭제시_존재하지_않는_테마면_예외를_응답한다() {
            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void 테마_삭제시_이미_예약이_존재하면_예외를_응답한다() {
            givenCreatedReservationTime();
            givenCreatedTheme();
            givenCreatedMember();
            givenCreatedReservation();

            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    class Database {

        @Test
        void 데이터베이스_연결을_검증한다() {
            try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
                assertThat(connection).isNotNull();
                assertThat(connection.getCatalog()).isEqualTo("DATABASE");
                assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void 방탈출_예약_목록을_조회한다() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                    "10:00");
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    "테마1", "설명1", "썸네일1");
            jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?, ?, ?)",
                    "브라운", "test@eamil.com", "pass1");
            jdbcTemplate.update("INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)",
                    "2025-08-05", 1, 1, 1);

            List<ReservationResponse> response = RestAssured.given().log().all()
                    .when().get("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value()).extract()
                    .jsonPath().getList(".", ReservationResponse.class);

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

            assertThat(response.size()).isEqualTo(count);
        }

        @Test
        void 방탈출_예약_목록을_생성_조회_삭제한다() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                    "10:00");
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    "테마1", "설명1", "썸네일1");
            jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?, ?, ?)",
                    "브라운", "test@email.com", "pass1");
            String token = givenLoginToken();

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", token)
                    .body(reservation)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
            assertThat(count).isEqualTo(1);

            RestAssured.given().log().all()
                    .when().delete("/reservations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
            Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
            assertThat(countAfterDelete).isEqualTo(0);
        }
    }

    private void givenCreatedReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private void givenCreatedTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private void givenCreatedMember() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private String givenLoginToken() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(auth)
                .when().post("/login")
                .then()
                .extract().response().cookie("token");
    }

    private void givenCreatedReservation() {
        String token = givenLoginToken();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
