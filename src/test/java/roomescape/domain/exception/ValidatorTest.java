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
}
