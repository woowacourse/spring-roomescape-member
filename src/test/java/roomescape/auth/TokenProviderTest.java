package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.auth.exception.AuthorizationException;

class TokenProviderTest {
    private static final String SECRET = "secretsecretsecretsecretsecretsecretsecretsecret";

    private TokenProvider tokenProvider = new TokenProvider(SECRET, 10000L);

    @Test
    void 토큰이_만료되면_예외가_발생한다() {
        tokenProvider = new TokenProvider(SECRET, 0L);
        String token = tokenProvider.createToken("1");

        assertThatThrownBy(() -> tokenProvider.extractSubject(token))
                .isExactlyInstanceOf(AuthorizationException.class)
                .hasMessageContaining("만료된 토큰입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 토큰이_비어있으면_예외가_발생한다(String token) {
        assertThatThrownBy(() -> tokenProvider.extractSubject(token))
                .isExactlyInstanceOf(AuthorizationException.class)
                .hasMessageContaining("토큰이 비어있습니다.");
    }

    @Test
    void 올바르지_않은_토큰이면_예외가_발생한다() {
        String invalidSecret = "invalidinvalidinvalidinvalidinvalidinvalidinvalidinvalid";
        TokenProvider invalidTokenProvider = new TokenProvider(invalidSecret, 10000L);
        String invalidToken = invalidTokenProvider.createToken("1");

        assertThatThrownBy(() -> tokenProvider.extractSubject(invalidToken))
                .isExactlyInstanceOf(AuthorizationException.class)
                .hasMessageContaining("올바르지 않은 토큰입니다.");
    }

    @Test
    void 토큰이_올바르면_subject_클레임을_추출한다() {
        String subject = "test";
        String token = tokenProvider.createToken(subject);

        String extractedSubject = tokenProvider.extractSubject(token);

        assertThat(extractedSubject).isEqualTo(subject);
    }
}
