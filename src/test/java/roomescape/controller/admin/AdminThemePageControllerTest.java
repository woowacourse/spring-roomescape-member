package roomescape.controller.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemePageControllerTest {

    @Test
    @DisplayName("/admin/themeResponse 페이지를 응답한다.")
    void responseThemePage() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
