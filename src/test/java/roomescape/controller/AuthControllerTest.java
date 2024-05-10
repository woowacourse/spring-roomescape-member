package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    @Test
    @DisplayName("로그인을 한다.")
    void login() {
        memberRepository.save(new Member("test123@example.com", "password", "test", Role.NORMAL));

        LoginRequest request = new LoginRequest("test123@example.com", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.header("Set-Cookie")).contains("token="),
                () -> assertThat(response.header("Set-Cookie")).contains("Path=/"),
                () -> assertThat(response.header("Set-Cookie")).contains("HttpOnly")
        );
    }

    @Test
    @DisplayName("로그인 여부를 확인한다.")
    void check() {
        memberRepository.save(new Member("test123@example.com", "password", "test", Role.NORMAL));
        TokenResponse tokenResponse = authService.createToken(new LoginRequest("test123@example.com", "password"));
        String token = tokenResponse.accessToken();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("cookie", "token=" + token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.header("Transfer-Encoding")).contains("chunked"),
                () -> assertThat(response.body().asString()).contains("test")
        );
    }
}
