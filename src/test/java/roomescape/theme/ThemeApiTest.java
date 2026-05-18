package roomescape.theme;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class ThemeApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("사용자는 활성화된 테마 목록만 조회할 수 있다")
    void findActiveThemes() {
        createActiveTheme("활성 테마");
        createTheme("비활성 테마");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("활성 테마"))
                .body("[0].isActive", is(true));
    }
}
