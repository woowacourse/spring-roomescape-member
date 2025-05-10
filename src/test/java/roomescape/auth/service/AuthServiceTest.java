package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.infrastructure.jwt.TokenProvider;
import roomescape.auth.stub.StubTokenProvider;
import roomescape.auth.web.controller.request.LoginRequest;
import roomescape.auth.web.controller.response.MemberNameResponse;
import roomescape.fixture.MemberDbFixture;
import roomescape.member.domain.Member;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberDbFixture memberDbFixture;

    @TestConfiguration
    static class AuthServiceTestConfig {
        @Primary
        @Bean
        public TokenProvider stubTokenProvider() {
            return new StubTokenProvider();
        }
    }

    @Test
    void 로그인한다() {
        Member user = memberDbFixture.유저1_생성();
        LoginRequest request = new LoginRequest(user.getEmail(), user.getPassword());

        String accessToken = authService.login(request);

        assertThat(accessToken).isEqualTo("user_stub_token");
    }

    @Test
    void 로그인한_사용자인지_확인한다() {
        Member user = memberDbFixture.유저1_생성();

        MemberNameResponse response = authService.check(user.getId());

        assertThat(response.name()).isEqualTo(user.getName());
    }

    @Test
    void 관리자인지_확인한다__관리자면_true() {
        Member admin = memberDbFixture.관리자_생성();
        String token = authService.login(new LoginRequest(admin.getEmail(), admin.getPassword()));

        boolean isAdmin = authService.isAdmin(token);

        assertThat(isAdmin).isTrue();
    }

    @Test
    void 관리자인지_확인한다__관리자가_아니면_false() {
        Member user = memberDbFixture.유저1_생성();
        String token = authService.login(new LoginRequest(user.getEmail(), user.getPassword()));

        boolean isAdmin = authService.isAdmin(token);

        assertThat(isAdmin).isFalse();
    }

    @Test
    void 토큰에서_회원_ID를_추출한다() {
        Member user = memberDbFixture.유저1_생성();
        String token = authService.login(new LoginRequest(user.getEmail(), user.getPassword()));

        Long memberId = authService.getMemberId(token);

        assertThat(memberId).isEqualTo(StubTokenProvider.STUB_MEMBER_ID);
    }

    @Test
    void 쿠키에서_토큰을_추출한다() {
        String token = "user_stub_token";
        Cookie[] cookies = {new Cookie("token", token)};

        String extractedToken = authService.extractTokenFromCookie(cookies);

        assertThat(extractedToken).isEqualTo(token);
    }
}
