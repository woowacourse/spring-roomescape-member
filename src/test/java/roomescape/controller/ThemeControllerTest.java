package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private ThemeController themeController;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("모든 테마 정보를 조회한다.")
    void readThemes() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void createThemes() {
        Map<String, String> params = Map.of(
                "name", "테마명",
                "description", "설명",
                "thumbnail", "http://testsfasdgasd.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/themes/1");
    }
}
