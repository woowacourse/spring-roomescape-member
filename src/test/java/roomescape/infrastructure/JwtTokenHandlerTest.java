package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.AuthorizationException;

class JwtTokenHandlerTest {

    @Test
    @DisplayName("올바른 jwt 토큰으로 인증 조회를 성공한다.")
    void checkJwtToken_Success() {
        // given
        JwtTokenHandler jwtTokenHandler = new JwtTokenHandler("secret", 3600000L);
        String token = jwtTokenHandler.createToken(new Member(1L, "naknak", Role.MEMBER));

        // when
        Member member = jwtTokenHandler.getMember(token);

        // then
        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(new Member(1L, "naknak", Role.MEMBER));
    }

    @Test
    @DisplayName("올바르지 않은 jwt 토큰으로 인증 조회를 할 시 예외가 발생한다.")
    void checkJwtToken_Failure() {
        // given
        JwtTokenHandler jwtTokenHandler1 = new JwtTokenHandler("secret", 3600000L);
        JwtTokenHandler jwtTokenHandler2 = new JwtTokenHandler("otherSecret", 3600000L);
        String token = jwtTokenHandler1.createToken(new Member(1L, "naknak", Role.MEMBER));

        // when & then
        assertThatThrownBy(() -> jwtTokenHandler2.getMember(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("인증 정보를 확인할 수 없습니다.");
    }

    @Test
    @DisplayName("만료된 jwt 토큰으로 인증 조회를 할 시 예외가 발생한다.")
    void checkJwtTokenExpire_Failure() {
        // given
        JwtTokenHandler jwtTokenHandler = new JwtTokenHandler("secret", -3600000L);
        String token1 = jwtTokenHandler.createToken(new Member(1L, "naknak", Role.MEMBER));

        // when & then
        assertThatThrownBy(() -> jwtTokenHandler.getMember(token1))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("인증이 만료되었습니다.");
    }
}
