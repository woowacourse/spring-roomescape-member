package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginControllerTest {

    @Test
    @DisplayName("로그인 요청 성공 시 토큰 쿠키를 반환한다")
    void loginSuccessReturnsTokenCookie() {
        Map<String, Object> body = Map.of(
                "password", "password",
                "email", "admin@email.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token");
    }

    @Test
    @DisplayName("토큰 쿠키는 기본 설정이 되어 있다")
    void loginTokenCookieHasDefaultSettings() {
        Map<String, Object> body = Map.of(
                "password", "password",
                "email", "admin@email.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .header("Keep-Alive", "timeout=60")
                .header("Set-Cookie", Matchers.containsString("Path=/"))
                .header("Set-Cookie", Matchers.containsString("HttpOnly"));
    }
}
