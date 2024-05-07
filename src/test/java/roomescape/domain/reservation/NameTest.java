package roomescape.domain.reservation;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    @ParameterizedTest
    @ValueSource(strings = {"123", "   ", "\uD83E\uDD79"})
    @DisplayName("이름에 한글 또는 영어가 아닌 문자가 포함되면 예외가 발생한다")
    void koreanOrEnglish(String value) {
        Assertions.assertThatThrownBy(() -> new Name(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("이름: %s, 이름은 영어 또는 한글만 가능합니다.", value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefghijk"})
    @DisplayName("예약이 두 글자 미만 혹은 열 글자 초과면 예외가 발생한다")
    void nameLength(String value) {
        Assertions.assertThatThrownBy(() -> new Name(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("이름은 %d~%d자만 가능합니다.", MIN_LENGTH, MAX_LENGTH));
    }
}
