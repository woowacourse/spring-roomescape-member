package roomescape.domain.theme;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.Name;

class DescriptionTest {
    private static final int MAX_LENGTH = 10;
    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefghi"})
    @DisplayName("테마 설명이 열 미만이 예외가 발생한다")
    void nameLength(String value) {
        Assertions.assertThatThrownBy(() -> new Description(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("설명은 %d글자 이상 입력해주세요.", MAX_LENGTH));
    }

}
