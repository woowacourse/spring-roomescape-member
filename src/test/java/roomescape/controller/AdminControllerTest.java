package roomescape.controller;

import static roomescape.TestFixture.ADMIN_PARAMETER_SOURCE;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.RESERVATION_TIME_PARAMETER_SOURCE;
import static roomescape.TestFixture.ROOM_THEME_PARAMETER_SOURCE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
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
        LoginRequest loginRequest = new LoginRequest("hkim1109@naver.com", "qwer1234");
        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
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
        LoginRequest loginRequest = new LoginRequest("hkim1109@naver.com", "qwer1234");
        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/admin/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.FORBIDDEN.value());
    }

    private AdminReservationRequest createReservationRequest(
            LocalDate date, SqlParameterSource memberSource) {
        Long memberId = createMember(jdbcTemplate, memberSource);
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        return new AdminReservationRequest(date, memberId, timeId, themeId);
    }

    private long createMember(JdbcTemplate jdbcTemplate, SqlParameterSource memberSource) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(memberSource)
                .longValue();
    }

    private long createReservationTime(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(RESERVATION_TIME_PARAMETER_SOURCE)
                .longValue();
    }

    private long createTheme(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(ROOM_THEME_PARAMETER_SOURCE)
                .longValue();
    }
}
