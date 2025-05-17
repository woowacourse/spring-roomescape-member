package roomescape.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CookieExtractorTest {

    @DisplayName("조건에 맞는 쿠키를 추출한다.")
    @Test
    void extract() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("user", "testUser");
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // when
        Cookie actual = CookieExtractor.extract(request, value -> value.getName().equals("user"));

        // then
        assertThat(actual.getValue()).isEqualTo("testUser");
    }

    @DisplayName("조건에 해당하는 쿠키가 없다면 예외가 발생한다.")
    @Test
    void extractOrThrowsWhenNoCookies() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("user", "testUser");
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // when & then
        assertThatThrownBy(() -> CookieExtractor.extract(request, value -> value.getName().equals("token")))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("쿠키에서 token을 추출한다.")
    @Test
    void extractToken() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("token", "jwtTokenValue");
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // when
        String actual = CookieExtractor.extractToken(request);

        // then
        assertThat(actual).isEqualTo("jwtTokenValue");
    }
}
