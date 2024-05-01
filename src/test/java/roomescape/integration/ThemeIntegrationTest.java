package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class ThemeIntegrationTest extends IntegrationTest {
    @Test
    void 테마_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
