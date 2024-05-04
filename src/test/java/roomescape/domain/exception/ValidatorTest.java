package roomescape.domain.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidatorTest {
    @DisplayName("인자 중 null이 있을 시 에러를 던진다.")
    @Test
    void validateNonNull() {
        assertThatThrownBy(() -> Validator.nonNull(null, "안녕", 1L))
                .isInstanceOf(IllegalNullArgumentException.class);
    }

    @DisplayName("인자 중 빈 값이 있을 시 에러를 던진다.")
    @Test
    void notEmpty() {
        assertThatThrownBy(() -> Validator.notEmpty("", "안녕"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 값이 존재합니다.");
    }
}
