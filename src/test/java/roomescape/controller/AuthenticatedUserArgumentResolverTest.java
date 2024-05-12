package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import roomescape.LoginUtils;
import roomescape.domain.Member;
import roomescape.repository.member.MemberRepository;

import static roomescape.InitialDataFixture.USER_1;
import static roomescape.service.CookieService.TOKEN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //TODO 테스트와 프로덕션이 같은 port를 사용중
class AuthenticatedUserArgumentResolverTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("쿠키가 없는 유저가 접근 시 요청이 거부된다.")
    @Test
    void hasNoCookieUser() {
        RestAssured.get("/login/check")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 쿠키를 가진 유저가 접근 시 요청이 거부된다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"invalidCookie", "     "})
    void hasInvalidCookieUser(String invalidToken) {
        RestAssured.given().cookie(TOKEN, invalidToken)
                .get("/login/check")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("쿠키가 있는 유저가 접근 시 요청이 처리된다.")
    @Test
    void hasCookieUser() {
        Member member = memberRepository.save(USER_1);
        String token = LoginUtils.loginAndGetToken(member);

        RestAssured.given()
                .cookie(TOKEN, token)
                .get("/login/check")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}