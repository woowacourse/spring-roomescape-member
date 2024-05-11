package roomescape.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;

    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void nullCheck() {
        Assertions.assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "qweqweqwe"})
    @DisplayName("이름 길이가 범위에서 벗어난다면 예외가 발생한다")
    void formatCheck(String value) {
        Assertions.assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("이름 길이는 %d 이하, %d 이상만 가능합니다", MIN_LENGTH, MAX_LENGTH));
    }

}
