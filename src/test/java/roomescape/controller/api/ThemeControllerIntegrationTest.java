package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/truncate.sql")
class ThemeControllerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into theme(theme_name, description, thumbnail) values ('공포', '공포입니다.', '123')");
        jdbcTemplate.update("insert into theme(theme_name, description, thumbnail) values ('해피', '해피입니다.', '456')");
    }

    @AfterEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM member");
    }

    @Test
    @DisplayName("새로운 테마 저장을 요청할 수 있다")
    void should_SaveNewTheme() {
        ThemeRequest themeRequest = new ThemeRequest("콜리테마", "브로콜리입니다.", "썸네일");
        Map<String, Object> request = new HashMap<>();
        request.put("name", themeRequest.name());
        request.put("description", themeRequest.description());
        request.put("thumbnail", themeRequest.thumbnail());

        ThemeResponse response = RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", ThemeResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo(themeRequest.name()),
                () -> assertThat(response.description()).isEqualTo(themeRequest.description()),
                () -> assertThat(response.thumbnail()).isEqualTo(themeRequest.thumbnail())
        );
    }

    @Test
    @DisplayName("모든 테마 목록을 반환할 수 있다")
    void should_GetAllThemes() {
        //given
        String sql = "SELECT count(*) FROM theme";
        int themeCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        List<ThemeResponse> responses = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", ThemeResponse.class);

        //then
        assertThat(responses).hasSize(themeCount);
    }

    @Test
    void findThemeRanking() {
        //given 테마1 - 2회  / 테마2 - 1회
        jdbcTemplate.update("insert into reservation_time(start_at) values ('09:00')");
        jdbcTemplate.update("insert into member(member_name, email, password, role) " +
                "values ('coli1', 'kkwoo001021@naver.com', 'rlarjsdn1021!', 'USER'), " +
                "('coli2', 'kkwoo1021@hanmail.net', 'rlarjsdn1021!', 'ADMIN')");
        LocalDate dayBeforeYesterDay = LocalDate.now().minusDays(2L);
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        String sql = "insert into reservation(date, time_id, theme_id, member_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, dayBeforeYesterDay, 1, 1, 1);
        jdbcTemplate.update(sql, yesterday, 1, 1, 1);
        jdbcTemplate.update(sql, yesterday, 1, 2, 1);

        //when
        List<ThemeResponse> responses = RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ThemeResponse.class);

        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.get(0).id()).isEqualTo(1L),
                () -> assertThat(responses.get(1).id()).isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("특정 id를 가진 테마를 삭제할 수 있다")
    void should_DeleteTheme_When_Give_ThemeId() {
        //given
        String sql = "SELECT count(*) FROM theme";
        int firstCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        //then
        int secondCount = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(firstCount).isEqualTo(secondCount + 1);
    }
}