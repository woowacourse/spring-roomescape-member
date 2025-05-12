package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.SignUpRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest {

    @Test
    @DisplayName("로그인 시 헤더에 토큰을 반환한다.")
    void loginTest() {
        ApiTestFixture.signUpUser("abc@naver.com", "password", "vector");

        Response response = ApiTestHelper.post(
                        "/auth/login",
                        new LoginMemberRequest("abc@naver.com", "password")
                )
                .statusCode(200)
                .extract().response();

        String setCookie = response.getHeader("Set-Cookie");
        Assertions.assertThat(setCookie).isNotNull();
    }

    @Test
    @DisplayName("로그인 체크 시 인증하고 이름을 반환한다.")
    void loginCheckTest() {
        ApiTestFixture.signUpAdmin("admin@naver.com", "password", "vector");
        String token = ApiTestFixture.loginAndGetToken("admin@naver.com", "password");

        ApiTestHelper.get("/auth/login/check", token)
                .statusCode(200)
                .body("name", is("vector"));
    }


    @Test
    @DisplayName("회원 가입 성공하면 201을 반환한다")
    void signUpTest() {
        SignUpRequest response = new SignUpRequest("new@naver.com", "pass123", "newbie");

        ApiTestHelper.post("/signup", response)
                .statusCode(201);
    }
}
