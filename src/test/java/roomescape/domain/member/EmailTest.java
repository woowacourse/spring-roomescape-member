package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @DisplayName("이메일 길이가 50자를 초과하면 예외를 발생한다.")
    @Test
    void invalidLengthTest() {
        String input = "a".repeat(40) + "@" + "test12.com";
        assertThatCode(() -> new Email(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 50자 이하여야 합니다.");
    }

    @DisplayName("이메일 형식이 올바르지 않으면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"test", "test@", "test.com", "test@.com", "test@com"})
    void invalidFormatTest(String input) {
        assertThatCode(() -> new Email(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @DisplayName("빈 문자열이나 null이면 예외를 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void nullEmptyTest(String input) {
        assertThatCode(() -> new Email(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 필수 입력값 입니다.");
    }
}
