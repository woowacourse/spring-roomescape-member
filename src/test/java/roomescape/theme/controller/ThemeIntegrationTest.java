package roomescape.theme.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.config.TestConfig;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeResponse;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("테마 목록의 조회 시 DB에 저장된 테마 목록을 반환한다")
    @Test
    void get_themes_test() {
        // when
        List<ThemeResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(themes.size()).isEqualTo(count);
    }

    @DisplayName("인기 테마의 목록을 조회한다")
    @Test
    void get_theme_rankings_test() {
        // when
        List<ThemeRankingResponse> themeRanks = RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeRankingResponse.class);

        // then
        assertThat(themeRanks).containsExactly(
                new ThemeRankingResponse("레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨4 탈출", "우테코 레벨4를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
    }

    @DisplayName("테마를 생성하면 DB에 테마 데이터가 저장된다")
    @Test
    void add_theme_test() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨7 탈출");
        params.put("description", "우테코 레벨7를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(7));

        // then
        String name = jdbcTemplate.queryForObject("SELECT name FROM theme WHERE id = ?", String.class, 7L);

        assertThat(name).isEqualTo("레벨7 탈출");
    }

    @DisplayName("테마를 삭제하면 DB의 테마 데이터가 삭제된다")
    @Test
    void delete_theme_test() {
        // when
        RestAssured.given().log().all()
                .when().delete("/themes/6")
                .then().log().all()
                .statusCode(204);

        Boolean actual = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM theme WHERE id = ?)",
                Boolean.class, 6);

        // then
        assertThat(actual).isFalse();
    }

}
