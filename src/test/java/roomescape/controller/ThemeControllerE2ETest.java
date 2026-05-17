package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerE2ETest {

    @DisplayName("모든 테마를 조회한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하는_모든_테마를_조회한다() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("인기 테마를 조회한다")
    @Test
    @Sql("/data.sql")
    void 인기_테마_조회() {
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("id", contains(1, 2, 3, 6, 5, 4, 8, 7, 10, 9));
    }

    @DisplayName("테마 ID로 상세 정보를 조회한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 테마_ID로_상세_정보를_조회한다() {
        RestAssured.given().log().all()
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("공포의 병원"));
    }

    @DisplayName("존재하지 않는 테마 ID 조회 시 422 Unprocessable Entity를 응답한다")
    @Test
    void 존재하지_않는_테마_ID_조회_시_422를_응답한다() {
        RestAssured.given().log().all()
                .when().get("/themes/9999")
                .then().log().all()
                .statusCode(422);
    }
}
