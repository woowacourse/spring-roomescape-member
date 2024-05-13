package roomescape.controller.api.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.InitialDataFixture;
import roomescape.domain.Theme;
import roomescape.repository.theme.ThemeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ThemeControllerTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "테마 이름");
        params.put("description", "테마 설명");
        params.put("thumbnail", "테마 썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("중복된 테마 이름을 추가하는 경우 bad request 상태코드를 반환한다.")
    void duplicatedTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", InitialDataFixture.THEME_1.getName().getName());
        params.put("description", "테마 설명");
        params.put("thumbnail", "테마 썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void getThemes() {
        List<Theme> themes = themeRepository.findAll();
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(themes.size()));
    }

    @Test
    @DisplayName("id와 매칭되는 테마를 삭제한다.")
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/themes/11")
                .then().log().all()
                .statusCode(204);
    }
}
