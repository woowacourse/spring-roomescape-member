package roomescape.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.global.ApiHelper;
import roomescape.member.domain.Role;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.fixture.MemberFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    private static final MemberFixture memberFixture = new MemberFixture();

    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() {
        // given
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password", Role.USER);

        // when - then
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register)
                .then().log().all()
                .statusCode(201);
    }

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

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    void getMembersTest() {
        // given
        RegisterRequest register1 = memberFixture.registerRequest("name1", "email1@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register1);
        RegisterRequest register2 = memberFixture.registerRequest("name2", "email2@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register2);
        RegisterRequest register3 = memberFixture.registerRequest("name3", "email3@email.com", "password", Role.USER);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register3);

        LoginRequest login = memberFixture.loginRequest("email1@email.com", "password");
        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");

        // when - then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }
}
