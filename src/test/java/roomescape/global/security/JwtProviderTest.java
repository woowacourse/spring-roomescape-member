package roomescape.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.auth.exception.TokenIsEmptyException;
import roomescape.auth.exception.TokenParseFailedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("정삭적으로 토큰을 생성한다")
    @Test
    void createToken() {
        // given
        Member member = Member.createWithId(1L, "에드", "test@email.com", "password", Role.USER);

        // when
        String token = jwtProvider.createToken(member);

        // then
        assertThat(token).isNotNull();
    }

    @DisplayName("토큰에서 멤버의 id를 추출한다")
    @Test
    void getMemberId() {
        // given
        long memberId = 1L;
        Member member = Member.createWithId(memberId, "에드", "test@email.com", "password", Role.USER);
        String token = jwtProvider.createToken(member);

        // when
        Long actual = jwtProvider.getMemberId(token);

        // then
        assertThat(actual).isEqualTo(memberId);
    }

    @DisplayName("토큰에서 멤버의 role 이름을 추출한다")
    @Test
    void getRoleName() {
        // given
        Role role = Role.USER;
        Member member = Member.createWithId(1L, "에드", "test@email.com", "password", role);
        String roleName = role.getName();
        String token = jwtProvider.createToken(member);

        // when
        String actual = jwtProvider.getRoleName(token);

        // then
        assertThat(actual).isEqualTo(roleName);
    }

    @DisplayName("토큰이 null이면 예외를 발생시킨다")
    @Test
    void nullToken() {
        // given
        String token = null;

        // when // then
        assertThatThrownBy(() -> jwtProvider.getMemberId(token))
                .isInstanceOf(TokenIsEmptyException.class);
    }

    @DisplayName("토큰 파싱에 실패하면 예외를 발생시킨다")
    @Test
    void invalidToken() {
        // given
        String token = "잘못된 토큰";

        // when // then
        assertThatThrownBy(() -> jwtProvider.getMemberId(token))
                .isInstanceOf(TokenParseFailedException.class);
    }
}