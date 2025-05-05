package roomescape.presentation.controller.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_2;
import static roomescape.testFixture.Fixture.RESERVATION_3;
import static roomescape.testFixture.Fixture.RESERVATION_BODY;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

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
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.reservation.presentation.dto.response.ReservationDetailResponse;
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
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약 추가 api 호출 시, db에 정상적으로 추가된다.")
    @Test
    public void request_addReservation() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertMember(jdbcTemplate,  MEMBER_1);
        long memberId = MEMBER_1.getId();

        String token = jwtTokenProvider.createToken(String.valueOf(memberId));

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
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        Long id = RESERVATION_1.getId();
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("모든 예약을 조회하는 api")
    @Test
    void getAllReservations() {
        JdbcHelper.insertReservations(jdbcTemplate, RESERVATION_1, RESERVATION_2, RESERVATION_3);

        List<ReservationDetailResponse> responses =
                RestAssured.given().log().all()
                        .when().get("/reservations")
                        .then().log().all()
                        .statusCode(200).extract()
                        .jsonPath().getList(".", ReservationDetailResponse.class);

        assertThat(responses.size()).isEqualTo(3);
    }

    @DisplayName("중복된 일시와 테마로 예약 생성 시 예외 발생")
    @Test
    void error_when_duplicateReservation() {
        // given
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        long memberId = 1L;
        String token = jwtTokenProvider.createToken(String.valueOf(memberId));

        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_1.getReservationTime().getId(),
                "themeId", RESERVATION_1.getTheme().getId()
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
        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_1.getReservationTime().getId(),
                "themeId", RESERVATION_1.getTheme().getId()
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
