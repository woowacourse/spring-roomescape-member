package roomescape.theme.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
                .body("runtime", hasItem(1));
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_예약이_많은_테마_상위_10개를_조회한다() {
        RestAssured.given().log().all()
                .queryParam("days", 7)
                .queryParam("limits", 10)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("$", hasSize(5))
                .body("[0].name", is("테마5"))
                .body("[1].name", is("테마4"))
                .body("[2].name", is("테마3"))
                .body("[3].name", is("테마2"))
                .body("[4].name", is("테마1"));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 10, days: 0보다 커야 합니다",
            "7, 0, limits: 0보다 커야 합니다",
            "-1, 10, days: 0보다 커야 합니다",
            "7, -1, limits: 0보다 커야 합니다"
    })
    void 인기_테마_조회_조건이_1보다_작으면_400을_응답한다(int days, int limits, String message) {
        RestAssured.given().log().all()
                .queryParam("days", days)
                .queryParam("limits", limits)
                .when().get("/themes")
                .then().log().all()
                .statusCode(400)
                .body("message", is(message));
    }

    @Test
    void 날짜와_제한_중_날짜만_있으면_400을_응답한다() {
        RestAssured.given().log().all()
                .queryParam("days", 7)
                .when().get("/themes")
                .then().log().all()
                .statusCode(400)
                .body("message", is("날짜와 제한은 함께 입력해야 합니다."));
    }

    @Test
    void 날짜와_제한_중_제한만_있으면_400을_응답한다() {
        RestAssured.given().log().all()
                .queryParam("limits", 10)
                .when().get("/themes")
                .then().log().all()
                .statusCode(400)
                .body("message", is("날짜와 제한은 함께 입력해야 합니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"days", "limits"})
    void 인기_테마_조회_파라미터_형식이_올바르지_않으면_400을_응답한다(String parameterName) {
        RestAssured.given().log().all()
                .queryParam(parameterName, "abc")
                .queryParam(parameterName.equals("days") ? "limits" : "days", 10)
                .when().get("/themes")
                .then().log().all()
                .statusCode(400)
                .body("message", is(parameterName + ": 입력 형식이 잘못되었습니다."));
    }
}
