package roomescape.controller.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {

    @Autowired
    ThemeController themeController;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마 목록을 조회하면 200 과 테마 리스트를 응답한다.")
    void getThemes200AndThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    @DisplayName("테마를 추가하면 201 과 테마와 위치를 응답한다.")
    void addTheme201AndThemeAndLocation() {
        CreateThemeRequest request = new CreateThemeRequest("New theme", "New desc", "New thumb");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/themes/"))
                .body("name", is(request.name()))
                .body("description", is(request.description()))
                .body("thumbnail", is(request.thumbnail()));
    }

    @Test
    @DisplayName("존재하는 테마를 삭제하면 204 를 응답한다.")
    void deleteTheme204Present() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 404 를 응답한다.")
    void deleteTheme404NotExist() {
        RestAssured.given().log().all()
                .when().delete("/themes/0")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("이미 예약된 테마를 삭제하면 400 을 응답한다.")
    void deleteThemeExistReservation() {
        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("인기 테마 목록을 조회하면 200 과 테마 리스트를 응답한다.")
    void getPopularThemes() {
        RestAssured.given().log().all()
                .param("days", "3")
                .param("limit", "3")
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }
}
