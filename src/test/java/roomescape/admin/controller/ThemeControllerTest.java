package roomescape.admin.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    void 관리자_전체_테마_조회() throws Exception {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

    }

    @Test
    void 관리자_단일_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("name", is("은하수"));
    }
}
