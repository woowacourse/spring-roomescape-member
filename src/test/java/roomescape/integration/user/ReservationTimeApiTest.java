package roomescape.integration.user;

import static org.hamcrest.Matchers.is;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;

import io.restassured.RestAssured;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.MemberRole;
import roomescape.utility.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;

    @DisplayName("모든 예약 가능 시간을 조회할 수 있다")
    @Test
    void canGetReservationTimes() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());

        // when & then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약 가능 여부와 함께 예약 가능 시간을 조회할 수 있다 - 예약이 비어있는 시간 조회")
    @Test
    void canGetReservationPossibleTimes() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0).toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1, 1);

        // when & then
        RestAssured
                .given()
                .param("date", NEXT_DAY.toString())
                .param("themeId", 1)
                .log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).isBooked", is(true));
    }

    @DisplayName("예약 가능 여부와 함께 예약 가능 시간을 조회할 수 있다 - 예약이 차있는 시간 조회")
    @Test
    void canGetReservationImpossibleTimes() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0).toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(11, 0).toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");

        // when & then
        RestAssured
                .given()
                .param("date", NEXT_DAY.toString())
                .param("themeId", 1)
                .log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).isBooked", is(false));
    }
}
