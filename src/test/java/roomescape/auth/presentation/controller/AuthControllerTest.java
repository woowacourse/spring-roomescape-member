package roomescape.auth.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.global.ApiHelper;
import roomescape.member.domain.Role;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.fixture.MemberFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    private static final MemberFixture memberFixture = new MemberFixture();

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register);
        LoginRequest login = memberFixture.loginRequest("email@email.com", "password");

        // when - then
        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");

        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("로그인 체크 테스트")
    void checkLoginTest() {
        // given
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register);
        LoginRequest login = memberFixture.loginRequest("email@email.com", "password");

        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");

        // when
        MemberNameResponse response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().as(MemberNameResponse.class);

        // then
        assertThat(response.name()).isEqualTo("name");
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {
        // given
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register);
        LoginRequest login = memberFixture.loginRequest("email@email.com", "password");
        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");

        // when
        String cookie = RestAssured.given().log().all()
                .cookie("token", token)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200)
                .extract().response().getCookie("token");

        // then
        assertThat(cookie).isBlank();
    }
}
