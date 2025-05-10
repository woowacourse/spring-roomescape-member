package roomescape.integration.admin;

import static roomescape.test.fixture.DateFixture.NEXT_DAY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("테마를 추가할 수 있다")
    @Test
    void canAddTheme() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("location", "/theme/1");
    }

    @DisplayName("특정 ID의 테마를 삭제할 수 있다")
    @Test
    void canDeleteThemeById() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("ID를 통해 테마를 삭제할 때, 이미 해당 테마에 대한 예약이 존재하면 테마를 삭제할 수 없다")
    @Test
    void cannotDeleteThemeByIdWhenReservationExist() {
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1, 1);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }
}
