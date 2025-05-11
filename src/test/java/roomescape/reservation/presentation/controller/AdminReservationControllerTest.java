package roomescape.reservation.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.AbstractRestDocsTest;
import roomescape.DatabaseCleaner;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;
import roomescape.testFixture.JdbcHelper;

class AdminReservationControllerTest extends AbstractRestDocsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        databaseCleaner.clean();
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

        givenWithDocs("adminReservation-add")
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
