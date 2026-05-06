package roomescape.theme.controller;

import static org.hamcrest.Matchers.hasItem;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/create_theme.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ThemeControllerTest {

    @Test
    void 테마_목록을_조회한다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("공포의 저택"))
                .body("description", hasItem("어두운 저택에 숨겨진 비밀을 찾아 탈출하는 테마"))
                .body("thumbnailUrl", hasItem("https://example.com/themes/haunted-house.png"))
                .body("runtime", hasItem(1));
    }
}
