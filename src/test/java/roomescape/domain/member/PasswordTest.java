package roomescape.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 12;

    @Test
    @DisplayName("비밀번호가 null이면 예외가 발생한다")
    void nullCheck() {
        Assertions.assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567890abc"})
    @DisplayName("비밀번호 길이가 범위에서 벗어난다면 예외가 발생한다")
    void formatCheck(String value) {
        Assertions.assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("비밀번호 길이는 %d 이하, %d 이상만 가능합니다", MIN_LENGTH, MAX_LENGTH));
    }
}
