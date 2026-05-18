package roomescape.domain.theme.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(
                    LocalDate.of(2026, 5, 6)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("themes.size()", is(10))
                .body("themes[0].id", is(1))
                .body("themes[0].name", is("워너비"))
                .body("themes[0].description", is("워너비 테마입니다."))
                .body("themes[0].thumbnailUrl", is("https://example.com/wannabe.png"));
    }

    @Test
    @DisplayName("특정 테마의 예약 가능 시간을 조회한다.")
    void getAllThemeReservationTimes() {
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-05")
                .when().get("/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(6))
                .body("times.id", contains(1, 2, 3, 4, 5, 6))
                .body("times.startAt", contains(
                        "10:00",
                        "11:00",
                        "12:00",
                        "13:00",
                        "14:00",
                        "15:00"
                ))
                .body("times.isAvailable", contains(
                        false,
                        false,
                        false,
                        false,
                        false,
                        true
                ));
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void getPopularThemes() {
        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("popularThemes.size()", is(2))
                .body("popularThemes[0].id", is(1))
                .body("popularThemes[0].name", is("워너비"))
                .body("popularThemes[0].description", is("워너비 테마입니다."))
                .body("popularThemes[0].thumbnailUrl", is("https://example.com/wannabe.png"))
                .body("popularThemes[0].rank", is(1))
                .body("popularThemes[1].id", is(2))
                .body("popularThemes[1].name", is("공포의 지하실"))
                .body("popularThemes[1].description", is("지하실에서 탈출하세요."))
                .body("popularThemes[1].thumbnailUrl", is("https://example.com/basement.png"))
                .body("popularThemes[1].rank", is(2));
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

    @ParameterizedTest
    @CsvSource({
            "name, 테마 이름은 필수입니다.",
            "description, 테마 설명은 필수입니다.",
            "thumbnailUrl, 썸네일 URL은 필수입니다."
    })
    @DisplayName("테마 생성 시 필수 값이 null이면 요청을 거부한다.")
    void createTheme_throwsException_whenRequiredValueIsNull(String fieldName, String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "새로운 테마");
        params.put("description", "새로운 테마 설명입니다.");
        params.put("thumbnailUrl", "https://example.com/new-theme.png");
        params.put(fieldName, null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("유효하지 않은 요청입니다."))
                .body("details.size()", is(1))
                .body("details[0].field", is(fieldName))
                .body("details[0].message", is(message));
    }

    @ParameterizedTest
    @CsvSource({
            "name, 테마 이름은 필수입니다.",
            "description, 테마 설명은 필수입니다.",
            "thumbnailUrl, 썸네일 URL은 필수입니다."
    })
    @DisplayName("테마 생성 시 필수 값이 공백이면 요청을 거부한다.")
    void createTheme_throwsException_whenRequiredValueIsBlank(String fieldName, String message) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "새로운 테마");
        params.put("description", "새로운 테마 설명입니다.");
        params.put("thumbnailUrl", "https://example.com/new-theme.png");
        params.put(fieldName, " ");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("유효하지 않은 요청입니다."))
                .body("details.size()", is(1))
                .body("details[0].field", is(fieldName))
                .body("details[0].message", is(message));
    }

    @Test
    @DisplayName("테마 생성 시 여러 필수 값이 유효하지 않으면 모든 필드 오류를 반환한다.")
    void createTheme_returnsAllValidationErrors_whenMultipleRequiredValuesAreInvalid() {
        Map<String, String> params = new HashMap<>();
        params.put("name", " ");
        params.put("description", " ");
        params.put("thumbnailUrl", " ");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("유효하지 않은 요청입니다."))
                .body("details.size()", is(3))
                .body("details.field", containsInAnyOrder("name", "description", "thumbnailUrl"))
                .body("details.message", containsInAnyOrder(
                        "테마 이름은 필수입니다.",
                        "테마 설명은 필수입니다.",
                        "썸네일 URL은 필수입니다."
                ));
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
    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다.")
    void deleteThemeFailWhenReservationExists() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(409);
    }
}
