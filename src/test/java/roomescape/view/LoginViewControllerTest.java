package roomescape.view;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginViewControllerTest {

    @DisplayName("로그인 페이지를 정상적으로 반환한다")
    @Test
    void login_page_test() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML);
    }

}
