package roomescape.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.base.BaseTest;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest extends BaseTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("토큰을 생성한다.")
    void createTokenTest() {
        String actualToken = authService.createToken(new MemberRequest(member.getEmail(), member.getPassword()));

        assertThat(actualToken).isEqualTo(memberToken);
    }

    @Test
    @DisplayName("없는 이메일을 입력한 경우, 에러가 발생한다.")
    void wrongEmailTest() {
        String wrongEmail = "wrongemail@example.com";

        assertThatThrownBy(() -> authService.createToken(new MemberRequest(wrongEmail, member.getPassword())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("패스워드가 틀린 경우, 에러가 발생한다.")
    void wrongPasswordTest() {
        String wrongPassword = "1234";

        assertThatThrownBy(() -> authService.createToken(new MemberRequest(member.getEmail(), wrongPassword)))
                .isInstanceOf(InvalidPasswordException.class);
    }
}
