package roomescape.theme.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.theme.fixture.ThemeApiFixture.createTheme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    private final String activeThemeName = "활성 테마";
    private final String inactiveThemeName = "비활성 테마";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 활성화된 테마 목록만 조회한다.")
    void get_active_themes() {
        Integer activeThemeId = createTheme(activeThemeName);
        Integer inactiveThemeId = createTheme(inactiveThemeName);

        updateThemeStatus(activeThemeId, true);
        updateThemeStatus(inactiveThemeId, false);

        RestAssured.given().log().all()
                .when().get("/member/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("활성화된 테마가 없으면 빈 목록을 반환한다.")
    void get_active_themes_empty() {
        Integer themeId = createTheme(inactiveThemeName);
        updateThemeStatus(themeId, false);

        RestAssured.given().log().all()
                .when().get("/member/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private void updateThemeStatus(Integer themeId, boolean isActive) {
        Map<String, Boolean> params = new HashMap<>();
        params.put("isActive", isActive);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(themeId))
                .body("isActive", is(isActive));
    }

}
