package roomescape.user.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.presentation.fixture.ApiHelper;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        LoginRequest login = new LoginRequest("email@email.com", "password");

        // when - then
        String token = ApiHelper.post("/login", login)
                .then().log().all()
                .extract().as(TokenResponse.class).accessToken();

        assertThat(token).isNotBlank();
    }
}
