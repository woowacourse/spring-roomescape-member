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
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeAdminControllerTest {

    private final String themeName = "테마1";
    private final String themeDescription = "테마1 설명";
    private final String thumbnailUrl = "테마1 썸네일";
    private final String defaultThumbnailUrl = "DEFAULT_THUMBNAIL_URL";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 테마 목록을 조회한다.")
    void getThemes() {
        RestAssured.given().log().all()
                .when().get("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("관리자는 테마를 생성한다.")
    void createTheme() {
        createTheme(themeName, themeDescription, thumbnailUrl);

        RestAssured.given().log().all()
                .when().get("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("관리자는 단일 테마를 조회한다.")
    void getTheme() {
        Integer themeId = createTheme(themeName, themeDescription, thumbnailUrl);

        RestAssured.given().log().all()
                .when().get("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    @DisplayName("관리자는 테마 활성화 상태를 변경한다.")
    void updateThemeStatus() {
        Integer themeId = createTheme(themeName, themeDescription, thumbnailUrl);

        Map<String, Boolean> params = new HashMap<>();
        params.put("isActive", true);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(themeId))
                .body("isActive", is(true));

        RestAssured.given().log().all()
                .when().get("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(themeId))
                .body("isActive", is(true));
    }

    @Test
    @DisplayName("name이 비어 있으면 테마 생성에 실패한다.")
    void createThemeWithoutName() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("description", themeDescription);
        params.put("thumbnailUrl", thumbnailUrl);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("description이 비어 있으면 테마 생성에 실패한다.")
    void createThemeWithoutDescription() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", themeName);
        params.put("description", "");
        params.put("thumbnailUrl", thumbnailUrl);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("thumbnailUrl이 없으면 테마 생성에 실패한다.")
    void createThemeWithoutThumbnailUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", themeName);
        params.put("description", themeDescription);
        params.put("thumbnailUrl", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("thumbnailUrl이 비어있어도 기본 썸네일 테마를 생성한다.")
    void createThemeEmptyThumbnailUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", themeName);
        params.put("description", themeDescription);
        params.put("thumbnailUrl", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .body("thumbnailUrl", is(defaultThumbnailUrl))
                .statusCode(200);
    }

    private Integer createTheme(String name, String description, String thumbnailUrl) {
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

}
