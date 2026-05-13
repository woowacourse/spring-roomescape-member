package roomescape.user.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    void 사용자_트렌드_테마_조회() throws Exception {
        RestAssured.given().log().all()
                .when().get("/user/themes/trending?from=2026-05-01&to=2026-05-07&limit=10")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }
}
