package roomescape.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.LoginException;
import roomescape.member.domain.Member;

class JwtTokenContainerTest {

    private JwtTokenContainer jwtTokenContainer = new JwtTokenContainer(
            "aasdasdasadsadasdsadsadsadsdsadasfsdf134fsgdafgdfgs");

    @Test
    @DisplayName("정상적인 토큰을 반환한다.")
    void createJwtToken_test() {
        // given
        Member member = Member.createWithId(1L, "a", "a", "a");
        // when
        String jwtToken = jwtTokenContainer.createJwtToken(member);
        // then
        assertThat(jwtToken).isNotNull();
    }

    @Test
    @DisplayName("토큰이 유효하지 않은 경우 예외를 발생한다.")
    void validateToken_exception() {
        // given
        String strangeToken = "asdasdasdsad";
        // when & then
        assertThatThrownBy(() -> jwtTokenContainer.validateToken(strangeToken))
                .isInstanceOf(LoginException.class);
    }

    @Test
    @DisplayName("토큰이 유효한 경우 예외를 발생시키지 않는다.")
    void validateToken_test() {
        // given
        Member member = Member.createWithId(1L, "a", "a", "a");
        String jwtToken = jwtTokenContainer.createJwtToken(member);
        // when & then
        assertDoesNotThrow(() -> jwtTokenContainer.validateToken(jwtToken));
    }

    @Test
    @DisplayName("정상적인 토큰인 경우 맴버 아이디를 가져온다.")
    void getMemberId_test() {
        // given
        Member member = Member.createWithId(1L, "a", "a", "a");
        String jwtToken = jwtTokenContainer.createJwtToken(member);
        // when
        Long memberId = jwtTokenContainer.getMemberId(jwtToken);
        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("정상적이지 않은 토큰일 경우 아이디를 가져오는 도중 예외를 발생한다.")
    void getMemberId_exception() {
        // given
        String strangeToken = "asdasdasdsad";
        // when
        assertThatThrownBy(() -> jwtTokenContainer.getMemberId(strangeToken))
                .isInstanceOf(LoginException.class);
    }

    @Test
    @DisplayName("정상적인 토큰인 경우 맴버 이름를 가져온다.")
    void getMemberName_test() {
        // given
        Member member = Member.createWithId(1L, "a", "a", "a");
        String jwtToken = jwtTokenContainer.createJwtToken(member);
        // when
        String name = jwtTokenContainer.getMemberName(jwtToken);
        // then
        assertThat(name).isEqualTo("a");
    }

    @Test
    @DisplayName("정상적이지 않은 토큰일 경우 이름을 가져오는 도중 예외를 발생한다.")
    void getMemberName_exception() {
        // given
        String strangeToken = "asdasdasdsad";
        // when
        assertThatThrownBy(() -> jwtTokenContainer.getMemberName(strangeToken))
                .isInstanceOf(LoginException.class);
    }

}