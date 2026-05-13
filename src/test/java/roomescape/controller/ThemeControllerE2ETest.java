package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerE2ETest {

    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하는_모든_테마를_조회한다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @Sql("/data.sql")
    void 인기_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("id", contains(1, 2, 3, 6, 5, 4, 8, 7, 10, 9));
    }
}