package roomescape.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.dto.response.MemberResponseDto;
import roomescape.domain.member.service.MemberService;
import roomescape.global.dto.TokenRequest;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest {

    @Autowired
    private MemberService memberService;

    private MemberResponseDto savedMember;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_NAME = "Test User";

    @BeforeEach
    void setUp() {
        // 테스트용 회원 생성
        MemberRequestDto memberRequestDto = new MemberRequestDto(
            TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
        savedMember = memberService.saveMember(memberRequestDto);
    }

    @Test
    @DisplayName("로그인에 성공하면 토큰이 담긴 쿠키를 받는다")
    void login_success_returns_token_cookie() {
        TokenRequest tokenRequest = new TokenRequest(TEST_EMAIL, TEST_PASSWORD);

        Response response = given().log().all()
            .contentType(ContentType.JSON)
            .body(tokenRequest)
            .when().post("/login")
            .then().log().all()
            .statusCode(200)
            .extract().response();

        // 쿠키 존재 확인
        String cookie = response.getCookie("token");
        assertThat(cookie).isNotNull();
    }

    @Test
    @DisplayName("잘못된 이메일이나 비밀번호로 로그인하면 실패한다")
    void login_with_invalid_credentials_fails() {
        TokenRequest tokenRequest = new TokenRequest("wrong@example.com", "wrongpassword");

        given().log().all()
            .contentType(ContentType.JSON)
            .body(tokenRequest)
            .when().post("/login")
            .then().log().all()
            .statusCode(400); // 잘못된 요청으로 400 Bad Request 반환
    }

    @Test
    @DisplayName("로그인 상태 확인 API는 로그인된 사용자의 이름을 반환한다")
    void check_login_returns_user_name() {
        // 먼저 로그인하여 토큰 쿠키 획득
        TokenRequest tokenRequest = new TokenRequest(TEST_EMAIL, TEST_PASSWORD);
        Response loginResponse = given()
            .contentType(ContentType.JSON)
            .body(tokenRequest)
            .when().post("/login")
            .then().extract().response();

        String tokenCookie = loginResponse.getCookie("token");

        // 로그인 상태 확인 API 호출
        given().log().all()
            .cookie("token", tokenCookie)
            .when().get("/login/check")
            .then().log().all()
            .statusCode(200)
            .body("name", equalTo(TEST_NAME));
    }

    @Test
    @DisplayName("로그아웃하면 토큰 쿠키가 만료된다")
    void logout_expires_token_cookie() {
        // 먼저 로그인하여 토큰 쿠키 획득
        TokenRequest tokenRequest = new TokenRequest(TEST_EMAIL, TEST_PASSWORD);
        Response loginResponse = given()
            .contentType(ContentType.JSON)
            .body(tokenRequest)
            .when().post("/login")
            .then().extract().response();

        String tokenCookie = loginResponse.getCookie("token");

        // 로그아웃 API 호출
        Response logoutResponse = given().log().all()
            .cookie("token", tokenCookie)
            .when().post("/logout")
            .then().log().all()
            .statusCode(200)
            .extract().response();

        Cookie token = logoutResponse.detailedCookie("token");
        long maxAge = token.getMaxAge();
        assertThat(maxAge).isEqualTo(0);
    }
}
