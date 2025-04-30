package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationRequestDtoTest {

    @Test
    @DisplayName("name 필드가 null일 경우 예외가 발생한다.")
    void failIfNameFieldIsNull() {
        assertThatThrownBy(() -> {
            new ReservationRequestDto(null, LocalDate.of(2025, 4, 30), 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 name 입력입니다.");
    }

    @Test
    @DisplayName("date 필드가 null일 경우 예외가 발생한다.")
    void failIfDateFieldIsNull() {
        assertThatThrownBy(() -> {
            new ReservationRequestDto("moda", null, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 date 입력입니다.");
    }

    @ParameterizedTest
    @DisplayName("timeId 필드가 1 미만일 경우 예외가 발생한다.")
    @ValueSource(longs = {0, -1})
    void failIfTimeIdFieldIsNull(long id) {
        assertThatThrownBy(() -> {
            new ReservationRequestDto("moda", LocalDate.of(2025, 4, 30), id);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 timeId 입력입니다.");
    }
}
