package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationRequestDtoTest {

    @ParameterizedTest
    @DisplayName("어떤 필드가 null일 경우 예외가 발생한다.")
    @CsvSource(value = {":2025-04-27:1", "moda::1", "moda:2025-04-27:0"}, delimiter = ':')
    void failIfFieldIsNull(String name, LocalDate date, long timeId) {
        assertThatThrownBy(() -> {
            new ReservationRequestDto(name, date, timeId);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
