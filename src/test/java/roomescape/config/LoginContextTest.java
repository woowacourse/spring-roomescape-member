package roomescape.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.business.domain.member.MemberRole;
import roomescape.business.domain.member.SignUpMember;
import roomescape.persistence.fakerepository.FakeMemberRepository;

class LoginContextTest {

    private FakeMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberRepository.save(new SignUpMember("벨로", "bello@email.com", "password"));
    }

    @DisplayName("쿠키에서 AccessToken을 읽고 로그인 정보를 가져온다.")
    @Test
    void getLoginMember() {
        // given
        String rawToken = AccessToken.create(new JwtPayload(1L, MemberRole.ADMIN.value())).getValue();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", rawToken));
        LoginContext loginContext = new LoginContext(memberRepository);

        // when
        LoginMember loginMember = loginContext.getLoginMember(request);

        // then
        assertAll(
                () -> assertEquals(1L, loginMember.id()),
                () -> assertEquals("벨로", loginMember.name()),
                () -> assertEquals(MemberRole.MEMBER, loginMember.role())
        );
    }
}
