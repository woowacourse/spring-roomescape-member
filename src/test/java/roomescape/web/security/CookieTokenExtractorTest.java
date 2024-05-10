package roomescape.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CookieTokenExtractorTest {

    @Test
    @DisplayName("쿠키에 있는 토큰을 추출한다.")
    void extract() {
        CookieTokenExtractor extractor = new CookieTokenExtractor();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies())
                .thenReturn(new Cookie[]{new Cookie("token", "expected")});

        String result = extractor.extract(request);

        assertThat(result).isEqualTo("expected");
    }

    @Test
    @DisplayName("쿠키에 토큰이 없다면 null을 반환한다.")
    void cantExtract() {
        CookieTokenExtractor extractor = new CookieTokenExtractor();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies())
                .thenReturn(new Cookie[]{});

        String result = extractor.extract(request);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("토큰이 아무것도 존재하지 않으면 null을 반환한다.")
    void cantExtract2() {
        CookieTokenExtractor extractor = new CookieTokenExtractor();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies())
                .thenReturn(null);

        String result = extractor.extract(request);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("request가 존재하지 않으면 null을 반환한다.")
    void cantExtract3() {
        CookieTokenExtractor extractor = new CookieTokenExtractor();

        String result = extractor.extract(null);

        assertThat(result).isNull();
    }
}
