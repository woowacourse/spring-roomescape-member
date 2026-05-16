package roomescape.acceptance;

import static org.hamcrest.Matchers.contains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

public class PopularThemeAcceptanceTest {

    @Test
    @Sql("/data.sql")
    void 인기_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/api/themes/popular-themes")
                .then().log().all()
                .statusCode(200)
                .body("id", contains(1, 2, 3, 6, 5, 4, 8, 7, 10, 9));
    }
}
