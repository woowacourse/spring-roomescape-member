package roomescape.reservation.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import roomescape.member.domain.Role;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminReservationControllerTest {

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

    @DisplayName("관리자 권한으로 관리자 예약 추가 api 호출 시, db에 정상적으로 추가된다.")
    @Test
    public void request_addReservation() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertMember(jdbcTemplate, MEMBER_1);
        long memberId = MEMBER_1.getId();

        String payload = String.valueOf(memberId);
        String token = jwtTokenProvider.createToken(payload, Role.ADMIN);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(createAdminReservationBody())
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(getReservationsCount()).isEqualTo(1);
    }

    private Map<String, Object> createAdminReservationBody() {
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Map.of(
                "memberId", 1,
                "date", date,
                "timeId", 1,
                "themeId", 1
        );
    }

    private int getReservationsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
    }

}
