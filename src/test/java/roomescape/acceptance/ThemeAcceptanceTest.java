package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class ThemeAcceptanceTest extends AcceptanceTestSupport{

    @Test
    @DisplayName("이미 등록된 테마명으로 생성 요청 시 409 상태코드를 반환한다.")
    void duplicateThemeNameRequestTest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "공포");
        params.put("thumbnailUrl", "http://localhost:8080/roomescape.app/admin/themes");
        params.put("description", "공포_설명");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(ErrorMessage.DUPLICATE_THEME.getMessage()));
    }

    @Test
    @DisplayName("예약이 존재하는 테마 삭제 요청 시 409 상태코드를 반환한다.")
    void deleteThemeInUseRequestTest() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'http://localhost/thumbnail', '공포_설명', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, 'user_a', '2026-06-01', 1, 1)");

        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(ErrorMessage.THEME_IN_USE.getMessage()));
    }

    @Test
    @DisplayName("테마 생성 요청에 테마명이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullThemeNameRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "");
        params.put("thumbnailUrl", "http://localhost:8080/roomescape.app/admin/themes");
        params.put("description", "공포_설명");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("테마 이름은 필수입니다."));
    }

    @Test
    @DisplayName("테마 생성 요청에 설명이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullThemeDescriptionRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "공포");
        params.put("thumbnailUrl", "http://localhost:8080/roomescape.app/admin/themes");
        params.put("description", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("테마 설명은 필수입니다."));
    }

    @Test
    @DisplayName("테마 생성 요청에 테마명이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void invalidThumbnailFormatRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "공포");
        params.put("thumbnailUrl", "잘못된 형식");
        params.put("description", "공포_설명");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("유효한 URL 형식이 아닙니다."));
    }
}
