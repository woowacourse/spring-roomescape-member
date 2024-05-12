package roomescape.theme.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.dto.ThemeRequest;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findThemeListTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/themes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(15));
    }

    @Test
    void saveThemeTest() {
        ThemeRequest themeRequest = new ThemeRequest("정글 모험", "열대 정글의 심연을 탐험하세요.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .log().all()
                .when()
                .post("themes")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deleteThemeByIdTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("themes/12")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void findPopularThemeListTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("themes/popular")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
