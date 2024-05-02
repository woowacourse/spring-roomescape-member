package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("모든 테마 정보를 조회한다.")
    void readThemes() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void createThemes() {
        Map<String, String> params = Map.of(
                "name", "테마명",
                "description", "설명",
                "thumbnail", "http://testsfasdgasd.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/themes/1");
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteThemes() {
        Map<String, String> params = Map.of(
                "name", "테마명",
                "description", "설명",
                "thumbnail", "http://testsfasdgasd.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/themes/1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    /*
     *  reservationData DataSet ThemeID 별 reservation 개수
     *  5,4,2,5,2,3,1,1,1,1,1
     *  예약 수 내림차순 + ThemeId 오름차순 정렬 순서
     *  1, 4, 2, 6, 3, 5, 7, 8, 9, 10
     */
    @Test
    @DisplayName("예약 수 상위 10개 테마를 조회했을 때 내림차순으로 정렬된다. 만약 예약 수가 같다면, id 순으로 오름차순 정렬된다.")
    @Sql(scripts = "/reservationData.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void readTop10ThemesDescOrder() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .when().get("/themes/top?count=10")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("id", contains(1, 4, 2, 6, 3, 5, 7, 8, 9, 10));
    }
}
