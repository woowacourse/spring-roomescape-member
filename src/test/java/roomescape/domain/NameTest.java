package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {
    @DisplayName("이름의 2글자 미만 or 10글자를 초과할 시 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefghijk"})
    void validateNameLength(String name) {
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 이름은 2자 이상 10자 이하여야 합니다.");
    }
}
