package roomescape.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.auth.InvalidAuthException;

class JwtExtractorTest {

    private final JwtExtractor extractor = new JwtExtractor();

    @DisplayName("쿠키에서 토큰을 추출할 수 있다.")
    @Test
    void extractTokenFromCookie() {
        //given
        String name = "token";
        String value = "abcde";
        Cookie[] cookie = new Cookie[]{new Cookie(name, value)};

        //when
        String actual = extractor.extractTokenFromCookie(name, cookie);

        //then
        assertThat(actual).isEqualTo("abcde");
    }

    @DisplayName("추출할 토큰의 이름이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExtractTokenFromCookie() {
        //given
        String name = "token";
        String value = "abcde";
        Cookie[] cookie = new Cookie[]{new Cookie(name, value)};

        //when //then
        assertThatThrownBy(() -> extractor.extractTokenFromCookie("invalidToken", cookie))
                .isInstanceOf(InvalidAuthException.class)
                .hasMessage("토큰을 찾을 수 없습니다.");
    }

}
