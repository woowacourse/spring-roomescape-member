package roomescape.integration.user;

import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
public class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;

    @DisplayName("자신의 예약을 추가할 수 있다")
    @Test
    void canCreateReservation() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "이름1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now());

        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);

        Map<String, Object> params = new HashMap<>();
        params.put("date", NEXT_DAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("location", "/reservations/1");
    }

    @DisplayName("예약을 추가할 때, 과거의 날짜와 시간은 허용하지 않는다")
    @Test
    void cannotCreateReservationsWhenPastRequest() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "이름1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));

        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);

        Map<String, Object> params = new HashMap<>();
        params.put("date", YESTERDAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약을 추가할 때, 중복된 예약은 허용하지 않는다")
    @Test
    void cannotCreateReservationsWhenDuplicatedTime() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1, 1);

        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);

        Map<String, Object> params = new HashMap<>();
        params.put("date", NEXT_DAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
