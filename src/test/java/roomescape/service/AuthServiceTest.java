package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import roomescape.dao.FakeMemberDao;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.LoginFailedException;

class AuthServiceTest {

    private final AuthService authService;
    private final MemberDao memberDao;

    public AuthServiceTest() {
        this.memberDao = new FakeMemberDao();
        authService = new AuthService(
                memberDao,
                "secretKeyfjdlsfksjfkjdsfdsjfdjls",
                1800000L
        );
    }

    @Test
    void 이메일과_비밀번호로_토큰을_생성한다() {
        // given
        Member member = new Member(null, "name1", "email1@domain.com", "password1");
        memberDao.save(member);
        LoginRequest request = new LoginRequest("email1@domain.com", "password1");
        // when & then
        assertThatCode(() -> authService.createToken(request)).doesNotThrowAnyException();
    }

    @Test
    void 존재하지_않는_이메일인_경우_예외가_발생한다() {
        // given
        LoginRequest request = new LoginRequest("email1@domain.com", "password1");
        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void 잘못된_비밀번호인_경우_예외가_발생한다() {
        // given
        Member member = new Member(null, "name1", "email1@domain.com", "password1");
        memberDao.save(member);
        LoginRequest request = new LoginRequest("email1@domain.com", "password2");
        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(LoginFailedException.class);
    }
}