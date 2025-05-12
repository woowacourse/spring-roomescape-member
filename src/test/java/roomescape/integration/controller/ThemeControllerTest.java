package roomescape.integration.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.controller.ThemeController;
import roomescape.integration.service.stub.StubThemeService;

/**
 * 해당 클래스는 ThemeController 의 요청/응답 형식을 테스트합니다.
 * SpringBootTest 어노테이션을 사용하여 실제로 서버를 띄우는 통합 테스트입니다.
 * 단, StubThemeService 를 사용하여 프로덕션 Service 와 분리했고, 실제 DB에 접근하지 않고 테스트합니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
    void readAllTheme() {
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
    void readAllThemeList() {
        RestAssured.given().log().all()
                .when().get("/themes/lists?orderType=popular_desc&listNum=1")
                .then().log().all()
                .body("[0].name", equalTo("hippo"))
                .body("[0].description", equalTo("test description"))
                .body("[0].thumbnail", equalTo("test.jpg"))
                .statusCode(200);
    }
}
