package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LoginTest {
    private final String email = "example@test.com";
    private final String password = "1234";
    private final String name = "test";

    @BeforeAll
    void setup() {
        // signup
        Map<String, String> signupParams = Map.of(
                "email", email,
                "password", password,
                "name", name
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인 요청시 set-cookie로 토큰을 받을 수 있다.")
    @Test
    void setCookieHeaderWhenLogin() {
        // login
        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        String token = doLogin(loginParams);

        assertThat(token).isNotEmpty();
    }
        
    @DisplayName("쿠키가 존재하는 경우, 로그인 여부를 확인할 수 있다.")
    @Test
    void checkLoginAfterRequestLogin() {
        // given
        // login
        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        // when
        String token = doLogin(loginParams);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .get("/login/check")
                .then().log().all()
                .statusCode(200);
    }

    private String doLogin(Map<String, String> loginParams) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();
        return response.cookie("token");
    }
}
