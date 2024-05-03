package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.handler.BadRequestException;

class NameTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new Name("zeze"));
    }

    @DisplayName("빈문자열이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void EmptyValueThrowsException(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름에 null 혹은 빈문자열을 입력할 수 없습니다.");
    }

    @DisplayName("null이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void nullThrowsException(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름에 null 혹은 빈문자열을 입력할 수 없습니다.");
    }
}
