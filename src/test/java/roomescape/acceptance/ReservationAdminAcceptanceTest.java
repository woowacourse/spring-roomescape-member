package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.service.JwtProvider;
import roomescape.service.ReservationAdminService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAdminAcceptanceTest {

    @Autowired
    ReservationAdminService reservationAdminService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtProvider jwtProvider;

    private String token;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("인증되지 않은 사용자가 예약 저장을 시도하면 예외가 발생한다.")
    void test1() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("themeId", "1");
        params.put("timeId", "1");
        params.put("memberId", "1");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", "invalidToken")
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }

    private Long insertNewReservationWithJdbcTemplate(final Long timeId, final Long themeId, LocalDate date) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (name, date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, "히로");
            ps.setDate(2, Date.valueOf(date));
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            ps.setLong(5, 1L);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }


}
