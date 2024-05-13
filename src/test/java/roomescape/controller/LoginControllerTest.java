package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Role;
import roomescape.dto.MemberCheckResponse;
import roomescape.dto.MemberRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginControllerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자에 존재하는 아이디와 비밀번호를 입력하면 로그인이 정상적으로 된다.")
    void login_existMember() {
        // given
        MemberRequest memberRequest = new MemberRequest("password1", "member1@email.com");

        // when
        Cookies cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200).extract()
                .response().getDetailedCookies();

        boolean doesCookieHasToken = cookies.hasCookieWithName(JwtTokenProvider.TOKEN_COOKIE_NAME);

        // then
        assertThat(doesCookieHasToken).isTrue();
    }

    @Test
    @DisplayName("사용자에 존재하지 않는 아이디와 비밀번호를 입력하면 로그인이 정상적으로 되지 않는다.")
    void login_notExistMember_badRequest() {
        // given
        MemberRequest memberRequest = new MemberRequest("noPassword", "noEmail@email.com");

        // when
        Cookies cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(400).extract()
                .response().getDetailedCookies();

        boolean doesCookieHasToken = cookies.hasCookieWithName(JwtTokenProvider.TOKEN_COOKIE_NAME);

        // then
        assertThat(doesCookieHasToken).isFalse();
    }

    @Test
    @DisplayName("로그인 체크가 정상적으로 되었는지 확인한다.")
    void loginCheck() {
        // given
        String token = tokenProvider.createToken(1L, "사용자1", Role.MEMBER.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when
        MemberCheckResponse memberCheckResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().response().as(MemberCheckResponse.class);

        // then
        assertThat(memberCheckResponse.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그아웃이 정상적으로 되었는지 확인한다.")
    void logout() {
        // given
        String token = tokenProvider.createToken(1L, "사용자1", Role.MEMBER.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when
        Cookie cookiesAfterLogout = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200).extract()
                .response().getDetailedCookie("token");

        // then
        assertThat(cookiesAfterLogout.getValue()).isEmpty();
    }
}
