package roomescape.controller;

import static roomescape.TestFixture.ADMIN_PARAMETER_SOURCE;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.EMAIL_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.PASSWORD_FIXTURE;
import static roomescape.TestFixture.createMember;
import static roomescape.TestFixture.createReservationTime;
import static roomescape.TestFixture.createTheme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Date;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("어드민으로 예약 생성 테스트")
    @Test
    void adminReservation() {
        // given
        AdminReservationRequest reservationRequest
                = createReservationRequest(DATE_FIXTURE, ADMIN_PARAMETER_SOURCE);
        String cookie = getCookie();
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/admin/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("일반 멤버로 admin 예약을 생성하려 시도하면 예외가 발생한다.")
    @Test
    void adminReservationByNormal() {
        // given
        AdminReservationRequest reservationRequest
                = createReservationRequest(DATE_FIXTURE, MEMBER_PARAMETER_SOURCE);
        String cookie = getCookie();
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/admin/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("어드민으로 예약 필터링 후 조회 테스트")
    @Test
    void adminReservationsWithFilter() {
        // given
        Long memberId = createMember(jdbcTemplate, ADMIN_PARAMETER_SOURCE);
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        String cookie = getCookie();
        SimpleJdbcInsert reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", Date.valueOf("3000-12-12"))
                .addValue("member_id", memberId)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);
        SqlParameterSource outOfFilterParameterSource = new MapSqlParameterSource()
                .addValue("date", Date.valueOf("4000-12-12"))
                .addValue("member_id", memberId)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);
        reservationInsert.execute(parameterSource);
        reservationInsert.execute(outOfFilterParameterSource);
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/admin/reservations/filter?"
                        + "theme-id=" + themeId
                        + "&member-id=" + memberId
                        + "&date-from=" + Date.valueOf("2500-12-12")
                        + "&date-to=" + Date.valueOf("3500-12-12"))
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    private String getCookie() {
        LoginRequest loginRequest = new LoginRequest(EMAIL_FIXTURE, PASSWORD_FIXTURE);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
    }

    private AdminReservationRequest createReservationRequest(
            LocalDate date, SqlParameterSource memberSource) {
        Long memberId = createMember(jdbcTemplate, memberSource);
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        return new AdminReservationRequest(date, memberId, timeId, themeId);
    }
}
