package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.service.stub.StubThemeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ThemeControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ThemeController themeController() {
            return new ThemeController(new StubThemeService());
        }
    }

    @Test
    void createTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "hippo");
        params.put("description", "test description");
        params.put("thumbnail", "test.jpg");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .body("name", equalTo("hippo"))
                .body("description", equalTo("test description"))
                .body("thumbnail", equalTo("test.jpg"))
                .statusCode(201);
    }

    @Test
    void readTheme() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .body("[0].name", equalTo("hippo"))
                .body("[0].description", equalTo("test description"))
                .body("[0].thumbnail", equalTo("test.jpg"))
                .statusCode(200);
    }

    @Test
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void readThemeList() {
        RestAssured.given().log().all()
                .when().get("/themes/lists?orderType=popular_desc&listNum=1")
                .then().log().all()
                .body("[0].name", equalTo("hippo"))
                .body("[0].description", equalTo("test description"))
                .body("[0].thumbnail", equalTo("test.jpg"))
                .statusCode(200);
    }
}
