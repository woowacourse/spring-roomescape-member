package roomescape.controller;

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

import static org.hamcrest.Matchers.is;

@Sql(value = {"/data-reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("테마 컨트롤러")
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마 컨트롤러는 테마 추가 요청이 들어오면 저장 후 201을 반환한다.")
    @Test
    void createTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "리얼공포");
        params.put("description", "완전 무서움");
        params.put("thumbnail", "http://something");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 컨트롤러는 테마 조회 요청이 들어오면 200을 반환한다.")
    @Test
    void readThemes() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("테마 컨트롤러는 인기 테마 조회 요청이 들어오면 200을 반환한다.")
    @Test
    void readPopularThemes() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 컨트롤러는 테마 삭제 요청이 들어오면 204를 반환한다.")
    @Test
    void deleteTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(204);
    }
}
