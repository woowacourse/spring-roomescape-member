package roomescape.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginPageControllerTest {

    @DisplayName("로그인 페이지를 불러 온다")
    @Test
    void getLoginPageTest() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }
}
