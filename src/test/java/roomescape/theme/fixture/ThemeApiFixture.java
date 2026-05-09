package roomescape.theme.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class ThemeApiFixture {

    private static final String THEME_DESCRIPTION = "테마1 설명";
    private static final String THUMBNAIL_URL = "테마1 썸네일";

    private ThemeApiFixture() {
    }

    public static Integer createTheme(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", THEME_DESCRIPTION);
        params.put("thumbnailUrl", THUMBNAIL_URL);

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
