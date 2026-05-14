package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThemeAcceptanceTest extends AcceptanceTestSupport{

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
                .statusCode(400)
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
                .statusCode(400)
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
                .statusCode(400)
                .body("message", is("유효한 URL 형식이 아닙니다."));
    }
}
