package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름이 공백일 경우 예외가 발생한다.")
    void validateNull(String name) {
        assertThatThrownBy(() -> Theme.from(name, "", ""))
                .isInstanceOf(InvalidRequestException.class);
    }
}
