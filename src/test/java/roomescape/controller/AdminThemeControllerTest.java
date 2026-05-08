package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemeControllerTest {

    @Test
    void 테마를_추가한다() {
        // when & then
        createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs")
                .statusCode(201);
    }

    @Test
    void 테마를_삭제한다() {
        // given
        int themeId = createTheme("방탈출11", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs")
                .statusCode(201)
                .extract().path("id");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(204);
    }

    private ValidatableResponse createTheme(String name, String description, String thumbnail) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "description", description, "thumbnail", thumbnail))
                .when().post("/admin/themes")
                .then().log().all();
    }
}
