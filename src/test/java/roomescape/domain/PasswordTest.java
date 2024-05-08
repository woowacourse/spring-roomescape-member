package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.user.Password;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {
    @Test
    @DisplayName("8글자 이하면 예외를 발생합니다.")
    void throw_exception_when_length_is_not_more_than_eight() {
        assertThatThrownBy(() -> new Password("abcdefg"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
