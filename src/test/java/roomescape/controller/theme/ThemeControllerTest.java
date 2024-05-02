package roomescape.controller.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마 조회")
    void getThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 생성")
    void addTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "fall");
        params.put("description", "Escape from fall");
        params.put("thumbnail", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTS1xLa6fkaTXaopKK3zxar7JUCiP6Jy-pwMEMl02RwiQ&s");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 삭제")
    void deleteTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "fall");
        params.put("description", "Escape from fall");
        params.put("thumbnail", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTS1xLa6fkaTXaopKK3zxar7JUCiP6Jy-pwMEMl02RwiQ&s");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(204);
    }
}
