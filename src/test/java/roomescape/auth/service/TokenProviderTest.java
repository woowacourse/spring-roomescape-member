package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.domain.AuthInfo;
import roomescape.fixture.MemberFixture;
import roomescape.member.domain.Member;

class TokenProviderTest {

    private static final String SECRET_KEY = "secret";

    private TokenProvider tokenProvider;

    @Test
    @DisplayName("토큰의 비밀 키가 다른 경우 경우 예외를 반환한다.")
    void extractAuthInfo() {
        tokenProvider = new TokenProvider(SECRET_KEY, 1000);
        Member member = MemberFixture.getOneWithId(1L);
        String token = tokenProvider.createToken(member);

        assertThat(tokenProvider.extractAuthInfo(token))
                .isEqualTo(new AuthInfo(member.getId(), member.getName(), member.getMemberRole()));
    }

    @Test
    @DisplayName("토큰의 형식이 올바르지 않은 경우 예외를 반환한다.")
    void extractAuthInfo_WhenTokenIsMalformed() {
        tokenProvider = new TokenProvider(SECRET_KEY, 1);

        assertThatThrownBy(() -> tokenProvider.extractAuthInfo("ㅁㄴㅇㄹㅇㄹ"))
                .isInstanceOf(SecurityException.class)
                .hasMessage("토큰의 형식이 유효하지 않습니다. 다시 로그인해주세요.");
    }

    @Test
    @DisplayName("토큰이 만료되었을 경우 예외를 반환한다.")
    void extractAuthInfo_WhenTokenIsExpired() {
        tokenProvider = new TokenProvider(SECRET_KEY, 1);
        String token = tokenProvider.createToken(MemberFixture.getOneWithId(1L));

        assertThatThrownBy(() -> tokenProvider.extractAuthInfo(token))
                .isInstanceOf(SecurityException.class)
                .hasMessage("토큰이 만료되었습니다. 다시 로그인해주세요.");
    }

    @Test
    @DisplayName("토큰의 비밀 키가 다른 경우 경우 예외를 반환한다.")
    void extractAuthInfo_WhenTokenSignatureIsInvalid() {
        tokenProvider = new TokenProvider(SECRET_KEY, 1);
        TokenProvider tokenProviderWithOtherSecretKey = new TokenProvider("asdf", 1);
        String token = tokenProvider.createToken(MemberFixture.getOneWithId(1L));

        assertThatThrownBy(() -> tokenProviderWithOtherSecretKey.extractAuthInfo(token))
                .isInstanceOf(SecurityException.class)
                .hasMessage("토큰의 값을 인증할 수 없습니다. 다시 로그인해주세요.");
    }
}
