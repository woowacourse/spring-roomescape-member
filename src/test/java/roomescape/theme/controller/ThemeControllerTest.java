package roomescape.theme.controller;

import static org.hamcrest.Matchers.is;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    private final String activeThemeName = "활성 테마";
    private final String inactiveThemeName = "비활성 테마";
    private final String description = "테마 설명";
    private final String thumbnailUrl = "테마 썸네일";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 활성화된 테마 목록만 조회한다.")
    void getActiveThemes() {
        Integer activeThemeId = createTheme(activeThemeName);
        Integer inactiveThemeId = createTheme(inactiveThemeName);

        updateThemeStatus(activeThemeId, true);
        updateThemeStatus(inactiveThemeId, false);

        RestAssured.given().log().all()
                .when().get("/member/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(activeThemeId))
                .body("[0].name", is(activeThemeName))
                .body("[0].isActive", is(true));
    }

    @Test
    @DisplayName("활성화된 테마가 없으면 빈 목록을 반환한다.")
    void getActiveThemesEmpty() {
        Integer themeId = createTheme(inactiveThemeName);
        updateThemeStatus(themeId, false);

        RestAssured.given().log().all()
                .when().get("/member/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private Integer createTheme(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("thumbnailUrl", thumbnailUrl);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
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
