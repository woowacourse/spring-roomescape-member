package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        String memberSetUp = "insert into member (name, email, password, role) values ('moda', 'moda_email', 'moda_password', 'ADMIN')";
        String reservationTimeSetUp = "insert into reservation_time (start_at) values ('10:00')";
        String themeSetUp = "insert into theme (name, description, thumbnail) values ('theme_name', 'theme_description', 'theme_thumbnail')";
        String reservationSetUp = "insert into reservation (date, member_id, time_id, theme_id) values ('2025-08-04', 1, 1, 1)";
        jdbcTemplate.update(memberSetUp);
        jdbcTemplate.update(reservationTimeSetUp);
        jdbcTemplate.update(themeSetUp);
        jdbcTemplate.update(reservationSetUp);
    }

    @Test
    @DisplayName("사용자가 예약을 생성한다.")
    void postReservation() {
        //given
        String date = "2025-08-05";

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        Member member = new Member(1L, "moda", "moda_email", "moda_password", MemberRole.ADMIN);
        AccessToken accessToken = new AccessToken(member);

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken.getValue())
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "date", is(date),
                        "member.id", is(1),
                        "member.name", is("moda"),
                        "time.id", is(1),
                        "time.startAt", is("10:00"),
                        "theme.id", is(1),
                        "theme.name", is("theme_name")
                );
    }

    @Test
    @DisplayName("토큰이 없을 경우 예약 생성 시 예외가 발생한다.")
    void failPostReservation() {
        //given
        String date = "2025-08-05";

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }
}
