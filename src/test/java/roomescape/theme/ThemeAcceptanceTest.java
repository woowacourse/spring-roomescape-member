package roomescape.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findAll() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/themes")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(0));
        save();
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/themes")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void save() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "정글 모험");
        params.put("description", "열대 정글의 심연을 탐험하세요.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/themes")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @Test
    void delete() {
        save();
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/themes/1")
                .then()
                .log()
                .all()
                .statusCode(204);

        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/themes/1")
                .then()
                .log()
                .all()
                .statusCode(404);
    }
}
