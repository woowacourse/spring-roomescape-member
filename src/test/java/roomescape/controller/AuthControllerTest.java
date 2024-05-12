package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    void setUp() {
        // 테스트 계정 생성
        memberRepository.save(new Member("test123@example.com", "password", "test", Role.NORMAL));
    }

    @Test
    @DisplayName("로그인을 했을때 응답 헤더에 토큰이 존재한다.")
    void login() {
        // 로그인 요청
        LoginRequest request = new LoginRequest("test123@example.com", "password");

        // 로그인
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract();

        // 검증: 응답 헤더에 토큰이 존재하는지 확인
        assertAll(
                () -> assertThat(response.header("Set-Cookie")).contains("token="),
                () -> assertThat(response.header("Set-Cookie")).contains("Path=/"),
                () -> assertThat(response.header("Set-Cookie")).contains("HttpOnly")
        );
    }

    @Test
    @DisplayName("로그인 체크를 했을때 사용자 이름이 존재한다.")
    void check() {
        // 로그인
        TokenResponse tokenResponse = authService.createToken(new LoginRequest("test123@example.com", "password"));
        String token = tokenResponse.accessToken();

        // 로그인 체크 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("cookie", "token=" + token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        // 검증: 응답 바디에 사용자 이름이 존재하는지 확인
        assertAll(
                () -> assertThat(response.header("Transfer-Encoding")).contains("chunked"),
                () -> assertThat(response.body().asString()).contains("test")
        );
    }
}
