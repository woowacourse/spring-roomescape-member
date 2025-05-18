package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.error.AuthenticationException;
import roomescape.infra.SessionLoginRepository;
import roomescape.stub.StubMemberRepository;

class AuthServiceTest {

    private final Member savedMember = new Member(1L, "홍길동", "hong@example.com", "pw123", Role.USER);
    private final LoginInfo savedLoginInfo = new LoginInfo(savedMember.getId(), savedMember.getName(), savedMember.getRole());

    private final MemberRepository stubMemberRepository = new StubMemberRepository(savedMember);
    private final SessionLoginRepository sessionLoginRepository = mock(SessionLoginRepository.class);
    private final AuthService sut = new AuthService(stubMemberRepository, sessionLoginRepository);

    @DisplayName("로그인에 성공하면 세션에 로그인 정보가 저장된다")
    @Test
    void login() {
        // given
        var request = new AuthRequest(savedMember.getEmail(), savedMember.getPassword());

        // when
        sut.login(request);

        // then
        verify(sessionLoginRepository, times(1)).setLoginInfo(savedLoginInfo);
    }

    @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다")
    @Test
    void login_nonexistentEmail() {
        // given
        var request = new AuthRequest("unknown@example.com", "1234");

        // when // then
        assertThatThrownBy(() -> sut.login(request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    @DisplayName("비밀번호가 틀리면 예외가 발생한다")
    @Test
    void login_wrongPassword() {
        // given
        var request = new AuthRequest(savedMember.getEmail(), "wrong");

        // when // then
        assertThatThrownBy(() -> sut.login(request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("로그아웃하면 세션에서 로그인 정보가 제거된다")
    @Test
    void logout() {
        // given
        sessionLoginRepository.setLoginInfo(savedLoginInfo);

        // when
        sut.logout();

        // then
        verify(sessionLoginRepository, times(1)).clear();
    }
}
