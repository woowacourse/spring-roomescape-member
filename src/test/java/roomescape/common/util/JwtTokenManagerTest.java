package roomescape.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.LoginException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

class JwtTokenManagerTest {

    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(
            "aasdasdasadsadasdsadsadsadsdsadasfsdf134fsgdafgdfgs");

    @Test
    @DisplayName("정상적인 토큰을 반환한다.")
    void createJwtToken_test() {
        // given
        Member member = Member.createWithId(1L, "a", "a@com", "a", Role.USER);
        LocalDateTime dateTime = LocalDateTime.now();
        // when
        String jwtToken = jwtTokenManager.createJwtToken(member, dateTime);
        // then
        assertThat(jwtToken).isNotNull();
    }

    @Test
    @DisplayName("정상적인 토큰인 경우 맴버 아이디를 가져온다.")
    void validateTokenAndGetMemberId_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        Member member = Member.createWithId(1L, "a", "a@com", "a", Role.USER);
        String jwtToken = jwtTokenManager.createJwtToken(member, dateTime);
        // when
        Long memberId = jwtTokenManager.validateTokenAndGetMemberId(jwtToken);
        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("정상적이지 않은 토큰일 경우 아이디를 가져오는 도중 예외를 발생한다.")
    void validateTokenAndGetMemberId_exception() {
        // given
        String strangeToken = "asdasdasdsad";
        // when
        assertThatThrownBy(() -> jwtTokenManager.validateTokenAndGetMemberId(strangeToken))
                .isInstanceOf(LoginException.class);
    }

    @Test
    @DisplayName("정상적인 토큰인 경우 맴버 권한을 가져온다.")
    void validateTokenAndGetMemberRole_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        Member member = Member.createWithId(1L, "a", "a@com", "a", Role.USER);
        String jwtToken = jwtTokenManager.createJwtToken(member, dateTime);
        // when
        Role memberRole = jwtTokenManager.validateTokenAndGetMemberRole(jwtToken);
        // then
        assertThat(memberRole).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("정상적이지 않은 토큰일 경우 권한을 가져오는 도중 예외를 발생한다.")
    void validateTokenAndGetMemberRole_exception() {
        // given
        String strangeToken = "asdasdasdsad";
        // when
        assertThatThrownBy(() -> jwtTokenManager.validateTokenAndGetMemberRole(strangeToken))
                .isInstanceOf(LoginException.class);
    }
}
