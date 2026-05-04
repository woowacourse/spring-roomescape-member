package roomescape.theme.presentation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class AdminThemeControllerTest {

    @Test
    @DisplayName("테마 생성 요청 시 비어있는 요청이 오면 400 Bad Request가 발생한다.")
    void createTheme_MissingParameter_Throws400() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400);
    }
}
