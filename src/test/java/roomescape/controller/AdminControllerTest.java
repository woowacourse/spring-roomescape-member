package roomescape.controller;

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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.dto.request.AdminReservationRequest;

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
    void adminReservationTest() {
        // given
        AdminReservationRequest adminReservationRequest
                = createAdminReservationRequest(DATE_FIXTURE);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminReservationRequest)
                .when().post("/admin/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    private AdminReservationRequest createAdminReservationRequest(LocalDate date) {
        Long memberId = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(MEMBER_PARAMETER_SOURCE)
                .longValue();
        Long timeId = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(RESERVATION_TIME_PARAMETER_SOURCE)
                .longValue();
        Long themeId = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(ROOM_THEME_PARAMETER_SOURCE)
                .longValue();
        return new AdminReservationRequest(date, memberId, timeId, themeId);
    }
}
