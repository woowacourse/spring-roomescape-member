package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;

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
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("사용자 이름에 공백을 입력할 수 없습니다.");
    }
}
