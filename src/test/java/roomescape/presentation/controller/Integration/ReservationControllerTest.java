package roomescape.presentation.controller.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_BODY;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.createReservationById;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.auth.domain.User;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.presentation.dto.response.ReservationResponse;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약 추가 api 호출 시, db에 정상적으로 추가된다.")
    @Test
    public void request_addReservation() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);

        String email = "test@email.com";
        String password = "password";
        String name = "멍구";
        JdbcHelper.insertUser(jdbcTemplate, new User(email, password, name));
        String token = jwtTokenProvider.createToken(email);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(RESERVATION_BODY)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(getReservationsCount()).isEqualTo(1);
    }

    @DisplayName("id로 예약을 삭제하는 api")
    @Test
    void requestDeleteReservation() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservation(jdbcTemplate, RESERVATION_1);

        Long id = RESERVATION_1.getId();
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("모든 예약을 조회하는 api")
    @Test
    void getAllReservations() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        Reservation reservation1 = createReservationById(1L);
        Reservation reservation2 = createReservationById(2L);
        Reservation reservation3 = createReservationById(3L);
        JdbcHelper.insertReservations(jdbcTemplate, reservation1, reservation2, reservation3);

        List<ReservationResponse> reservations =
                RestAssured.given().log().all()
                        .when().get("/reservations")
                        .then().log().all()
                        .statusCode(200).extract()
                        .jsonPath().getList(".", ReservationResponse.class);

        assertThat(reservations.size()).isEqualTo(3);
    }

    @DisplayName("중복된 일시와 테마로 예약 생성 시 예외 발생")
    @Test
    void error_when_duplicateReservation() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "멍구";
        JdbcHelper.insertUser(jdbcTemplate, new User(email, password, name));

        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservation(jdbcTemplate, RESERVATION_1);

        String token = jwtTokenProvider.createToken(email);

        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_TIME_1.getId(),
                "themeId", THEME_1.getId()
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("토큰 정보가 없으면 로그인 필요 예외 발생")
    @Test
    void error_when_noLogin() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "멍구";
        JdbcHelper.insertUser(jdbcTemplate, new User(email, password, name));

        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservation(jdbcTemplate, RESERVATION_1);

        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_TIME_1.getId(),
                "themeId", THEME_1.getId()
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }

    private int getReservationsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
    }
}
