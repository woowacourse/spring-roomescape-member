package roomescape.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.global.ApiHelper;
import roomescape.member.presentation.dto.LoginRequest;
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
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password");

        // when - then
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register)
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password");
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register);
        LoginRequest login = memberFixture.loginRequest("email@email.com", "password");

        // when - then
        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");

        assertThat(token).isNotBlank();
    }
}
