package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ReservationCreationRequestTest {

    @DisplayName("null이거나 공백인 이름을 허용하지 않는다")
    @ParameterizedTest
    @NullAndEmptySource
    void validateFieldName(String invalidName) {
        assertThatThrownBy(() -> new ReservationCreationRequest(invalidName, LocalDate.now(), 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.");
    }

    @DisplayName("비어있는 날짜를 허용하지 않는다.")
    @Test
    void validateFieldDate() {
        assertThatThrownBy(() -> new ReservationCreationRequest("kim", null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜는 빈 값을 허용하지 않습니다.");
    }

    @DisplayName("비어있는 시간을 허용하지 않는다.")
    @Test
    void validateFieldTime() {
        assertThatThrownBy(() -> new ReservationCreationRequest("kim", LocalDate.now(), null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
    }
}
