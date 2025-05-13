package roomescape.integration.admin;

import static org.hamcrest.Matchers.is;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.TODAY;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
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
public class ReservationAplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;

    @DisplayName("모든 예약을 조회할 수 있다")
    @Test
    void canGetReservations() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자", "test@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(11, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, LocalDate.now(), 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, LocalDate.now(), 2, 1);

        // when & then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("필터를 적용해서 예약들을 조회할 수 있다")
    @Test
    void canGetReservationsByFilter() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자", "test@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, YESTERDAY, 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, TODAY, 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY, 1, 1);

        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 1);
        params.put("memberId", 1);
        params.put("dateFrom", TODAY.toString());
        params.put("dateTo", NEXT_DAY.toString());

        // when & then
        RestAssured.given().log().all()
                .queryParams(params)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("특정 회원의 예약을 추가할 수 있다")
    @Test
    void canCreateReservation() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자", "test@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.now());

        String accessToken = jwtTokenProvider.makeAccessToken(1L, "관리자", MemberRole.ADMIN);

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", NEXT_DAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .header("location", "/reservations/1");
    }

    @DisplayName("특정 ID의 예약을 삭제할 수 있다")
    @Test
    void canDeleteReservation() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자", "test@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1, 1);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
