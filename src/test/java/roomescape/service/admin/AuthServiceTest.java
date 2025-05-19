package roomescape.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.auth.JwtTokenProvider;
import roomescape.common.exception.auth.InvalidAuthException;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.auth.request.LoginRequest;
import roomescape.dto.auth.response.MemberNameResponse;
import roomescape.service.auth.AuthService;

class AuthServiceTest {

    private static final String SECRET_KEY = "my-test-secret-key-my-test-secret-key";

    private AuthService authService;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new FakeMemberDaoImpl();
        this.authService = new AuthService(new JwtTokenProvider(SECRET_KEY), memberDao);
    }

    @DisplayName("인증에 성공하면 Jwt 토큰을 발급한다.")
    @Test
    void creatJwtToken() {
        //given
        memberDao.save(Member.fromWithoutId("testName", "testEmail", "1234"));

        LoginRequest request = new LoginRequest("testEmail", "1234");

        //when
        String actual = authService.createToken(request);

        //then
        assertThat(actual).isNotNull();
    }

    @DisplayName("인증에 실패하면 Jwt 토큰을 발급하지 않고 예외가 발생한다.")
    @Test
    void doesNotCreatJwtToken() {
        //given
        memberDao.save(Member.fromWithoutId("testName", "testEmail", "1234"));
        LoginRequest request = new LoginRequest("testEmail", "1235");

        //when //then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(InvalidAuthException.class);

    }

    @DisplayName("Cookie로 사용자 인증정보 조회를 할 수 있다.")
    @Test
    void findMemberByToken() {
        //given
        memberDao.save(Member.fromWithoutId("testName", "testEmail", "1234"));
        LoginRequest request = new LoginRequest("testEmail", "1234");
        String token = authService.createToken(request);

        //when
        MemberNameResponse actual = authService.checkLogin(token);

        //then
        assertThat(actual.name()).isEqualTo("testName");
    }
}
