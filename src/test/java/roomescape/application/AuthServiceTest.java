package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.dto.request.TokenCreationRequest;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.Principal;
import roomescape.auth.TokenProvider;
import roomescape.support.annotation.ServiceTest;

@ServiceTest
@Sql("/member.sql")
class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenProvider tokenProvider;

    @Test
    void 사용자_인증이_통과되면_토큰을_반환한다() {
        TokenCreationRequest request = new TokenCreationRequest("prin@gmail.com", "1q2w3e4r!@");

        TokenResponse response = authService.authenticateMember(request);

        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"prin@gmail.com, invalid", "foo@gmail.com, 1q2w3e4r!@"})
    void 사용자_인증이_실패하면_예외를_발생시킨다(String email, String password) {
        TokenCreationRequest request = new TokenCreationRequest(email, password);

        assertThatThrownBy(() -> authService.authenticateMember(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 이메일이거나 비밀번호가 틀렸습니다.");
    }

    @Test
    void 토큰으로_사용자의_인증_정보를_생성한다() {
        String token = tokenProvider.createToken("1");

        Principal principal = authService.createPrincipal(token);

        assertThat(principal.getId()).isEqualTo(1L);
    }
}
