package roomescape.theme.presentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마를 생성하면 201과 생성된 테마를 반환한다")
    void createTheme() {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("테마A"));
    }

    @Test
    @DisplayName("테마 목록을 조회하면 200과 테마 목록을 반환한다")
    void getThemes() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes");

        given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("일주일간 인기 테마 목록을 조회하면 200을 반환한다")
    void getPopularThemes() {
        Long themeId = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");

        Long timeId = given().contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().extract().jsonPath().getLong("id");

        String yesterday = LocalDate.now().minusDays(1).toString();
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "포비",
                        "date", yesterday,
                        "timeId", timeId,
                        "themeId", themeId
                ))
                .when().post("/reservations");

        given().log().all()
                .when().get("/themes/weeks/top")
                .then().log().all()
                .statusCode(200)
                .body("[0].id", equalTo(themeId.intValue()));
    }

    @Test
    @DisplayName("테마를 삭제하면 204를 반환한다")
    void deleteTheme() {
        Long id = given().contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "테마A",
                        "description", "설명",
                        "thumbnailImageUrl", "https://example.com/a.png",
                        "durationTime", "01:00:00"
                ))
                .when().post("/admin/themes")
                .then().extract().jsonPath().getLong("id");

        given().log().all()
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(204);
    }
}
