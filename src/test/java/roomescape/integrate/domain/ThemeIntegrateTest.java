package roomescape.integrate.domain;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeIntegrateTest {

    static Map<String, String> params;

    static {
        params = new HashMap<>();
        params.put("name", "브라운");
        params.put("description", "브라운 방탈출");
        params.put("thumbnail", "hello");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("delete from reservation_time");
        jdbcTemplate.execute("delete from theme");
        jdbcTemplate.execute("delete from reservation");
    }

    @Test
    void 테마_추가_테스트() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 테마_삭제_테스트() {
        long deleteThemeId = 1L;
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/themes/" + deleteThemeId)
                .then().log().all()
                .statusCode(204);
    }
}
