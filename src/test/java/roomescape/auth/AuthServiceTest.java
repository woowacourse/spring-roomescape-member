package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.exception.custom.reason.auth.AuthNotExistsEmailException;
import roomescape.exception.custom.reason.auth.AuthNotValidPasswordException;
import roomescape.exception.custom.reason.auth.AuthNotValidTokenException;
import roomescape.member.FakeMemberRepository;
import roomescape.member.Member;

public class AuthServiceTest {

    private final AuthService authService;
    private final FakeMemberRepository fakeMemberRepository;
    private final JwtProvider jwtProvider;

    public AuthServiceTest() {
        fakeMemberRepository = new FakeMemberRepository();
        jwtProvider = new JwtProvider();
        authService = new AuthService(fakeMemberRepository, jwtProvider);
    }

    @Nested
    @DisplayName("토큰 발급")
    class GenerateToken {
        @DisplayName("토큰을 발급한다.")
        @Test
        void generateToken() {
            // given
            final LoginRequest request = new LoginRequest("admin@email.com", "pw1234");
            fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));

            // when
            final String actual = authService.generateToken(request);

            // then
            assertThat(jwtProvider.isValidToken(actual)).isTrue();
        }

        @DisplayName("유저 이메일이 존재하지 않는다면, 예외가 발생한다.")
        @Test
        void generateToken1() {
            // given
            final LoginRequest request = new LoginRequest("admin@email.com", "pw1234");

            // when & then
            assertThatThrownBy(() -> {
                authService.generateToken(request);
            }).isInstanceOf(AuthNotExistsEmailException.class);
        }

        @DisplayName("비밀번호가 일치하지 않는다면, 예외가 발생한다.")
        @Test
        void generateToken2() {
            // given
            final LoginRequest request = new LoginRequest("admin@email.com", "not matches password");
            fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));

            // when & then
            assertThatThrownBy(() -> {
                authService.generateToken(request);
            }).isInstanceOf(AuthNotValidPasswordException.class);
        }
    }

    @Nested
    @DisplayName("토큰으로 유저 정보 조회")
    class FindLoginMemberByToken {
        @DisplayName("토큰으로 유저 정보를 조회한다.")
        @Test
        void findLoginMemberByToken() {
            // given
            fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));
            final String token = jwtProvider.provideToken("admin@email.com");

            // when
            final LoginMember actual = authService.findLoginMemberByToken(token);

            // then
            assertThat(actual).isEqualTo(new LoginMember(1L, "부기", "admin@email.com"));
        }

        @DisplayName("토큰이 유효하지 않다면, 예외가 발생한다.")
        @Test
        void findLoginMemberByToken1() {
            // given
            fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));
            final String notValidToken = jwtProvider.provideToken("admin@email.com") + "a";

            // when & then
            assertThatThrownBy(() -> {
                authService.findLoginMemberByToken(notValidToken);
            }).isInstanceOf(AuthNotValidTokenException.class);
        }

        @DisplayName("토큰에 해당하는 이메일의 member가 존재하지 않는다면, 예외가 발생한다.")
        @Test
        void findLoginMemberByToken2() {
            // given
            final String token = jwtProvider.provideToken("admin@email.com");

            // when & then
            assertThatThrownBy(() -> {
                authService.findLoginMemberByToken(token);
            }).isInstanceOf(AuthNotExistsEmailException.class);
        }
    }
}
