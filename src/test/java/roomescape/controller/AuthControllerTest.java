package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Test
    @DisplayName("로그인 요청 성공 시 토큰 쿠키를 반환한다")
    void loginSuccessReturnsTokenCookie() {
        Map<String, Object> body = createLoginRequest();

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
        Map<String, Object> body = createLoginRequest();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .header("Keep-Alive", "timeout=60")
                .header("Set-Cookie", Matchers.containsString("Path=/"))
                .header("Set-Cookie", Matchers.containsString("HttpOnly"));
    }

    @Test
    @DisplayName("사용자는 로그인 쿠키로 인증할 수 있다")
    void authenticateLoginCookies() {
        // given
        Map<String, Object> body = createLoginRequest();

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .cookie("token");

        // when
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("name", is("테스트1"));
    }

    private Map<String, Object> createLoginRequest() {
        return Map.of(
                "password", "password1",
                "email", "test1@email.com"
        );
    }
}
