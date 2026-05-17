package roomescape.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/theme-integration-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 전체_테마를_조회한다() {
        // given & when
        var response = given().log().all()
            .when().get("/themes");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(3));
    }

    @Test
    void 관리자는_테마를_추가한다() {
        // given
        Map<String, Object> request = Map.of(
            "name", "신규테마",
            "description", "신규설명",
            "imageUrl", "https://example.com/img.jpg"
        );

        // when
        var response = given().log().all()
            .queryParam("role", "admin")
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes");

        // then
        response.then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue());
    }

    @Test
    void 관리자는_예약이_없는_테마를_삭제한다() {
        // given & when (theme_id=2는 예약 없음)
        var response = given().log().all()
            .queryParam("role", "admin")
            .when().delete("/themes/2");

        // then
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 예약이_있는_테마를_삭제하면_예외가_발생한다() {
        // given & when (theme_id=1은 예약 있음)
        var response = given().log().all()
            .queryParam("role", "admin")
            .when().delete("/themes/1");

        // then
        response.then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SqlMergeMode(MergeMode.OVERRIDE)
    @Sql(scripts = "/theme-popular-integration-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void 주간_인기_테마를_조회한다() {
        // given (테마9가 10건으로 1위, 테마0이 1건으로 10위)
        int limit = 10;

        // when
        var response = given().log().all()
            .queryParam("limit", limit)
            .when().get("/themes/popular/week");

        // then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("themes", hasSize(10))
            .body("themes[0].name", equalTo("테마9"))
            .body("themes[9].name", equalTo("테마0"));
    }

    @Test
    void 관리자가_아닌_경우_테마_추가가_불가하다() {
        // given
        Map<String, Object> request = Map.of(
            "name", "신규테마",
            "description", "신규설명",
            "imageUrl", "https://example.com/img.jpg"
        );

        // when
        var response = given().log().all()
            .queryParam("role", "user")
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/themes");

        // then
        response.then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 관리자가_아닌_경우_테마_삭제가_불가하다() {
        // given & when
        var response = given().log().all()
            .queryParam("role", "user")
            .when().delete("/themes/2");

        // then
        response.then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
