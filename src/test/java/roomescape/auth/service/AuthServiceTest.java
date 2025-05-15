package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.infra.JwtProvider;
import roomescape.member.Member;
import roomescape.member.Role;
import roomescape.member.dao.FakeMemberDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

    private JwtProvider jwtProvider;
    private FakeMemberDao fakeMemberDao;
    private AuthService authService;

    private final static Long MEMBER_ID = 1L;
    private final static String MEMBER_NAME = "이승연";
    private final static String MEMBER_EMAIL = "forarium20@gmail.com";
    private final static String MEMBER_PASSWORD = "123456";

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        fakeMemberDao = new FakeMemberDao(new Member(MEMBER_ID, MEMBER_NAME, MEMBER_EMAIL, MEMBER_PASSWORD, Role.USER));
        authService = new AuthService(jwtProvider, fakeMemberDao);
    }

    @Test
    void 사용자_정보를_통해_토큰을_생성할_수_있다() {
        // given
        LoginRequest loginRequest = new LoginRequest(MEMBER_EMAIL, MEMBER_PASSWORD);

        // when
        TokenResponse token = authService.createToken(loginRequest);

        // then
        String email = jwtProvider.getEmail(token.accessToken());
        assertThat(email).isEqualTo(MEMBER_EMAIL);
    }

}
