package roomescape.auth.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.member.model.MemberEmail;
import roomescape.member.model.MemberRole;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("사용자 정보를 입력하면 인증 토큰을 생성한다.")
    @Test
    void createTokenTest() {
        // Given
        final MemberEmail email = new MemberEmail("kelly6bf@gmail.com");
        final MemberRole role = MemberRole.USER;

        // When
        final AuthenticationToken token = tokenProvider.createToken(email, role);

        // Then
        final long expirationDay = token.getClaims().getExpiration().getTime();
        final long today = new Date().getTime();
        final int expirationPeriod = (int) (expirationDay - today) / (24 * 60 * 60 * 1000) + 1;

        assertAll(
                () -> assertThat(token.validate()).isTrue(),
                () -> assertThat(token.getClaims().getSubject()).isEqualTo(email.value()),
                () -> assertThat(token.getClaims().get("role")).isEqualTo(role.name()),
                () -> assertThat(expirationPeriod).isEqualTo(7)
        );
    }

    @DisplayName("AccessToken을 입력하면 Authentication 객체로 변환한다.")
    @Test
    void convertAuthenticationTokenTest() {
        // Given
        final MemberEmail email = new MemberEmail("kelly6bf@gmail.com");
        final MemberRole role = MemberRole.USER;
        final String accessToken = tokenProvider.createToken(email, role).getValue();

        // When
        final AuthenticationToken authenticationToken = tokenProvider.convertAuthenticationToken(accessToken);

        // Then
        assertAll(
                () -> assertThat(authenticationToken.getClaims().getSubject()).isEqualTo(email.value()),
                () -> assertThat(authenticationToken.getClaims().get("role")).isEqualTo(role.name())
        );
    }
}
