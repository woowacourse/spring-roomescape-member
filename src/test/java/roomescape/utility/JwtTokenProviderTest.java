package roomescape.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.MemberRole;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.exception.UnauthorizedException;

class JwtTokenProviderTest {

    private static final String secretKey = "test_secret_key";
    private static final long validityInMilliseconds = 72000;
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);

    @DisplayName("Access 토큰을 생성할 수 있다")
    @Test
    void canMakeAccessToken() {
        // when
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "이름", MemberRole.GENERAL);

        // then
        Date validity = new Date(new Date().getTime() + validityInMilliseconds);
        Claims tokenBody = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        assertAll(
                () -> assertThat(tokenBody.getSubject()).isEqualTo("1"),
                () -> assertThat(tokenBody.get("name")).isEqualTo("이름"),
                () -> assertThat(tokenBody.get("role")).isEqualTo(MemberRole.GENERAL.toString()),
                () -> assertThat(tokenBody.getExpiration().getTime())
                        .isBetween(validity.getTime() - 60000, validity.getTime())
        );
    }

    @DisplayName("토큰을 파싱할 수 있다")
    @Test
    void canParseToken() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "이름", MemberRole.GENERAL);

        // when
        AuthenticationInformation parsed = jwtTokenProvider.parseToken(accessToken);

        // then
        Date validity = new Date(new Date().getTime() + validityInMilliseconds);
        assertAll(
                () -> assertThat(parsed.id()).isEqualTo(1),
                () -> assertThat(parsed.name()).isEqualTo("이름"),
                () -> assertThat(parsed.role()).isEqualTo(MemberRole.GENERAL)
        );
    }

    @DisplayName("토큰을 파싱할 때, 토큰이 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotParseTokenBecauseOfInvalidToken() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "이름", MemberRole.GENERAL);
        String invalidToken = accessToken + "invalid";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(invalidToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("[ERROR] 유효하지 않은 인증정보입니다.");
    }

    @DisplayName("토큰을 파싱할 때, 토큰이 만료된 경우 예외를 발생시킨다")
    @Test
    void cannotParseTokenBecauseOfTokenExpiration() {
        // given
        long zeroValidityInMilliseconds = 0L;
        jwtTokenProvider = new JwtTokenProvider(secretKey, zeroValidityInMilliseconds);
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "이름", MemberRole.GENERAL);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(accessToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("[ERROR] 유효하지 않은 인증정보입니다.");
    }
}
