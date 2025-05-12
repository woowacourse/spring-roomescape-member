package roomescape.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.MemberProfileResponse;
import roomescape.auth.service.AuthService;
import roomescape.util.fixture.AuthFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginIntegrationTest {

    @Autowired
    private AuthService authService;

    private String token;

    @BeforeEach
    void setup() {
        token = AuthFixture.createUserToken(authService);
    }

    @DisplayName("로그인 후 응답 헤더에 토큰을 반환한다")
    @Test
    void login_test() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "rookie@woowa.com");
        params.put("password", "rookie123");

        // when
        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        // then
        assertAll(
                () -> assertThat(header).isNotNull(),
                () -> assertThat(header).contains("token="),
                () -> assertThat(header).contains("Path=/"),
                () -> assertThat(header).contains("HttpOnly")
        );
    }

    @DisplayName("로그인 비밀번호가 올바르지 않으면 예외가 발생한다")
    @Test
    void login_password_mismatch() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", "rookie@woowa.com");
        params.put("password", "invalidPassword");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("비밀번호가 일치하지 않습니다."));
    }

    @DisplayName("사용자의 정보를 가져온다")
    @Test
    void check_member_test() {
        // when
        MemberProfileResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(MemberProfileResponse.class);

        // then
        assertThat(response.name()).isEqualTo("사용자");
    }

    @DisplayName("로그인하지 않은 상태에서 사용자의 정보를 조회하면 예외가 발생한다")
    @Test
    void check_member_exception_test() {
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401)
                .body(equalTo("인증에 실패했습니다."));
    }

    @DisplayName("쿠키 내부에 토큰이 존재하지 않으면 예외가 발생한다")
    @Test
    void cookie_not_exists_exception() {
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401)
                .body(equalTo("인증에 실패했습니다."));
    }

    @DisplayName("로그아웃 요청 시 토큰 쿠키가 삭제된다")
    @Test
    void logout_test() {
        // when
        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().post("/logout")
                .then().log().all()
                .statusCode(204)
                .extract()
                .header("Set-Cookie");

        //then
        assertThat(header)
                .contains("token=")
                .contains("Max-Age=0");
    }

}
