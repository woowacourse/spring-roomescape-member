package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @DisplayName("유효하지 않은 이름 값으로 이름을 생성할 수 없다.")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "aaaaaaaaaaaaaaaaaaaaaaaa26"})
    @ParameterizedTest
    void createNameWithInvalidValue(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("유효한 값으로 이름을 생성할 수 있다.")
    @ValueSource(strings = {"a", "aaaaaaaaaaaaaaaaaaaaaaa25"})
    @ParameterizedTest
    void createNameWithValidValue(String value) {
        assertThatCode(() -> new Name(value))
                .doesNotThrowAnyException();
    }
}
