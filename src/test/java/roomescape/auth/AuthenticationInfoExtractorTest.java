package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AuthenticationInfoExtractorTest {
    @Autowired
    private AuthenticationInfoExtractor authenticationInfoExtractor;
    @Autowired
    private TokenProvider tokenProvider;

    @Test
    void request의_쿠키에서_회원_id를_추출한다() {
        String token = tokenProvider.createToken("1");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", token));

        long memberId = authenticationInfoExtractor.extractMemberId(request);

        assertThat(memberId).isEqualTo(1L);
    }
}
