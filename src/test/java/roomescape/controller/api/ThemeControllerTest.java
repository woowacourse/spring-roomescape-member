package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("존재하지 않는 테마 삭제 시 BadRequest 반환")
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
    @TestFactory
    Stream<DynamicTest> saveAndDeleteTheme() {
        return Stream.of(
                dynamicTest("테마를 추가한다", () -> {
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
                }),
                dynamicTest("테마를 삭제한다", () ->
                        RestAssured.given().log().all()
                                .when().delete("/themes/14")
                                .then().log().all()
                                .statusCode(204)
                )
        );
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
