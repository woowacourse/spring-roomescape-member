package roomescape.domain.theme.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminThemeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 테마를 생성한다.")
    void createTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "새로운 테마");
        params.put("description", "새로운 테마 설명입니다.");
        params.put("thumbnailUrl", "https://example.com/new-theme.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("name", is("새로운 테마"))
                .body("description", is("새로운 테마 설명입니다."))
                .body("thumbnailUrl", is("https://example.com/new-theme.png"));
    }

    @Test
    @DisplayName("관리자는 예약이 존재하지 않는 테마를 삭제한다.")
    void deleteTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "삭제할 테마");
        params.put("description", "삭제할 테마 설명입니다.");
        params.put("thumbnailUrl", "https://example.com/delete-theme.png");

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("관리자라도 예약이 존재하는 테마는 삭제할 수 없다.")
    void deleteThemeFailWhenReservationExists() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("관리자라도 유효하지 않은 입력값으로 테마를 생성하면 에러가 발생한다.")
    void createThemeWithInvalidInputThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "");
        params.put("description", "설명");
        params.put("thumbnailUrl", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }
}
