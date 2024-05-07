package roomescape.domain.theme;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {
    private static final int MAX_LENGTH = 10;
    @Test
    @DisplayName("테마 이름이 열 글자를 초과하면 예외가 발생한다")
    void nameLength() {
        Assertions.assertThatThrownBy(() -> new Name("abcdefghijk")).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("테마이름은 %d자까지 만 가능합니다.", MAX_LENGTH));
    }
}
