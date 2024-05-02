package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("존재하지 않는 테마 삭제")
    @Test
    void deletedReservationNotFound() {
        RestAssured.given().log().all()
                .when().delete("/themes/100")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마 목록 조회")
    @Test
    void getReservationsWhenEmpty() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(13));
    }

    @DisplayName("테마 추가 및 삭제")
    @Test
    void saveAndDeleteReservation() {
        final Map<String, Object> params = Map.of(
                "name", "테마테마",
                "description", "설명설명",
                "thumbnail", "썸네일썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/themes/14");

        RestAssured.given().log().all()
                .when().delete("/themes/14")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("인기 테마 조회")
    @Test
    void getPopularThemes() {
        RestAssured.given().log().all()
                .when().get("/themes/popular?date=2024-05-01")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }
}
