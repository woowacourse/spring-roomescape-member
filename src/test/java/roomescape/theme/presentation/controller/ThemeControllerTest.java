package roomescape.theme.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.snippet.Attributes.key;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.THEME_2;
import static roomescape.testFixture.Fixture.createTokenByMemberId;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.AbstractRestDocsTest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.testFixture.JdbcHelper;

public class ThemeControllerTest extends AbstractRestDocsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("테마 조회 요청 시 모든 테마를 응답한다")
    @Test
    void getAllThemes() {
        JdbcHelper.insertThemes(jdbcTemplate, THEME_1, THEME_2);

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        givenWithDocs("themes-getAll")
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("테마 생성 요청 시 생성된 테마 정보를 응답한다")
    @Test
    void createTheme() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "레벨2 탈출입니다.");
        params.put("thumbnail", "썸네일");

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        ExtractableResponse<Response> extractedResponse =
                givenWithDocs("themes-add")
                        .cookie("token", token)
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when().post("/themes")
                        .then().log().all()
                        .statusCode(201).extract();

        assertAll(
                () -> assertThat(extractedResponse.jsonPath().getString("name")).isEqualTo(params.get("name")),
                () -> assertThat(extractedResponse.jsonPath().getString("description")).isEqualTo(
                        params.get("description")),
                () -> assertThat(extractedResponse.jsonPath().getString("thumbnail")).isEqualTo(params.get("thumbnail"))
        );
    }

    @DisplayName("테마 삭제 요청 시 해당하는 id의 테마를 삭제한다")
    @Test
    void delete() {
        //given
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        int addedCount = getThemesCount();
        assertThat(addedCount).isEqualTo(1);

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        //when & then
        givenWithDocs("themes-deleteById")
                .cookie("token", token)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        int deletedCount = getThemesCount();
        assertThat(deletedCount).isEqualTo(0);
    }

    @DisplayName("테마 랭킹 조회 시 200 OK")
    @Test
    void getThemeRanking() {
        // given
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_2);

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        RestAssured.given(documentationSpec)
                .filter(document("themes-ranking",
                        queryParameters(
                                parameterWithName("startDate").description("시작 날짜").optional()
                                        .attributes(key("defaultValue").value("일주일 전 (7일 전)")),
                                parameterWithName("endDate").description("종료 날짜").optional()
                                        .attributes(key("defaultValue").value("어제 (1일 전)")),
                                parameterWithName("limit").description("조회할 개수").optional()
                                        .attributes(key("defaultValue").value("10"))
                        )
                ))
                .cookie("token", token)
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200);
    }

    private int getThemesCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);
    }
}
