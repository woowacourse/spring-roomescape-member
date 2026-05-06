package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    void 테마_추가() {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "방탈출1");
        themeParams.put("description", "다함께 탈출해요 방탈출.");
        themeParams.put("thumbnail", "https://asdfsdf.sdfs");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 테마_삭제() {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "방탈출11");
        themeParams.put("description", "다함께 탈출해요 방탈출.");
        themeParams.put("thumbnail", "https://asdfsdf.sdfs");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }
}
