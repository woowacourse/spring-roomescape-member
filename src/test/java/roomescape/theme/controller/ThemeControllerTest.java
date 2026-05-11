package roomescape.theme.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ThemeControllerTest {

    @Sql("/create_theme.sql")
    @Test
    void 테마_목록을_조회한다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("공포의 저택"))
                .body("description", hasItem("어두운 저택에 숨겨진 비밀을 찾아 탈출하는 테마"))
                .body("thumbnailUrl", hasItem("https://example.com/themes/haunted-house.png"))
                .body("runtime", hasItem(60));
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_예약이_많은_테마_상위_10개를_조회한다() {
        RestAssured.given().log().all()
                .queryParam("days", 7)
                .queryParam("limit", 10)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("$", hasSize(5))
                .body("[0].name", is("테마5"))
                .body("[1].name", is("테마4"))
                .body("[2].name", is("테마3"))
                .body("[3].name", is("테마2"))
                .body("[4].name", is("테마1"));
    }

}
