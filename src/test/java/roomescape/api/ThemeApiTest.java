package roomescape.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeRequest;

@Sql("/theme-api-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ThemeApiTest {

    @LocalServerPort
    int port;

    @Test
    void 테마_추가() {
        String name = "ted";
        String description = "설명설명설명설명설명설명설명";
        String thumbnail = "썸네일";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);
        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/1")
                .body("id", equalTo(1))
                .body("name", equalTo(name))
                .body("description", equalTo(description))
                .body("thumbnail", equalTo(thumbnail));
    }

    @Test
    void 테마_단일_조회() {
        String name = "ted";
        String description = "설명설명설명설명설명설명설명";
        String thumbnail = "썸네일";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes");

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo(name))
                .body("description", equalTo(description))
                .body("thumbnail", equalTo(thumbnail));
    }

    @Test
    void 테마_전체_조회() {
        String name = "ted";
        String description = "설명설명설명설명설명설명설명";
        String thumbnail = "썸네일";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes");

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 테마_삭제() {
        String name = "ted";
        String description = "설명설명설명설명설명설명설명";
        String thumbnail = "썸네일";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes");

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Sql("/theme-service-test-data.sql")
    @Test
    void 인기_테마_조회() {
        given().log().all()
                .port(port)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }
}
