package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DescriptionTest {

    @Test
    @DisplayName("설명에 null을 입력받으면 예외가 발생한다.")
    void createDescriptionByNullTest() {
        assertThatThrownBy(() -> new Description(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("설명에 빈 문자열, 공백 입력시 예외가 발생한다.")
    @ValueSource(strings = {"", " "})
    void createDescriptionByBlankTest(String input) {
        assertThatThrownBy(() -> new Description(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("설명에 공백을 포함할 수 있고 한글 또는 숫자가 아닌 값을 입력시 예외가 발생한다.")
    void createDescriptionByInvalidFormat() {
        assertThatThrownBy(() -> new Description("scare 이야기"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("설명이 50글자를 초과하면 예외가 발생한다.")
    void createDescriptionByLengthTest() {
        String input = "가나다라마바사아자차".repeat(5) + "1";

        assertThatThrownBy(() -> new Description(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
