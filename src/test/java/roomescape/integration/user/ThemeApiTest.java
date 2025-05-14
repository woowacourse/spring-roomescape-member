package roomescape.integration.user;

import static org.hamcrest.Matchers.is;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

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
public class ThemeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canGetThemes() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마3", "설명", "썸네일");

        // when & then
        RestAssured
                .given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("인기 테마를 조회할 수 있다")
    @Test
    void cnaGetTopThemes() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "인기테마", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "평범테마", "설명2", "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "인기없는테마", "설명3", "썸네일3");
        for (int i = 0; i < 10; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    "테마" + i, "설명", "썸네일");
        }

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0).toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(11, 0).toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(12, 0).toString());

        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 1, 1);
        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 2, 1);
        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 3, 1);
        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 1, 2);
        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 2, 2);
        jdbcTemplate.update("insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1L, YESTERDAY.toString(), 1, 3);

        // when & then
        RestAssured
                .given().log().all()
                .when().get("/themes/top")
                .then().log().all()
                .statusCode(200)
                .body("get(0).name", is("인기테마"))
                .body("get(1).name", is("평범테마"))
                .body("get(2).name", is("인기없는테마"))
                .body("size()", is(10));
    }
}
