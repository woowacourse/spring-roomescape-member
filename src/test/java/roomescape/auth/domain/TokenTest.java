package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenTest {
    @Test
    @DisplayName("유효한 문자열로 Token을 생성할 수 있다")
    void test1() {
        // given
        String value = "valid-token";
        Token expectedToken = new Token("valid-token");

        // when
        Token token = new Token(value);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("null 값으로 Token을 생성하면 예외가 발생한다")
    void test2() {
        // given
        String value = null;

        // when & then
        assertThatThrownBy(() -> new Token(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 토큰 값이 빈 값이어서는 안 됩니다.");
    }

    @Test
    @DisplayName("공백 문자열로 Token을 생성하면 예외가 발생한다")
    void test3() {
        // given
        String value = "   ";

        // when & then
        assertThatThrownBy(() -> new Token(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 토큰 값이 빈 값이어서는 안 됩니다.");
    }
}
