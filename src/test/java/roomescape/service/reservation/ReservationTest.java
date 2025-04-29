package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("예약자명은 최소 1글자, 최대 5글자가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"aaaaaa", " ", "   "})
    @NullAndEmptySource
    void testValidateName(String name) {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        // when
        // then
        assertThatThrownBy(() -> new Reservation(id, name, date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명은 최소 1글자, 최대 5글자여야합니다.");
    }

    @DisplayName("주어진 날짜와 시간보다 이전인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"2025-04-29;09:59;False", "2025-04-29;10:00;True", "2025-04-29;10:01;True",
            "2025-04-28;10:01;False", "2025-04-30;10:01;True"}, delimiterString = ";")
    void testIsBefore(LocalDate date, LocalTime time, boolean expected) {
        // given
        LocalDate reservationDate = LocalDate.of(2025, 4, 29);
        LocalTime reservationTime = LocalTime.of(10, 0);
        Reservation reservation = new Reservation(1L, "leo", reservationDate, new ReservationTime(1L, reservationTime));
        // when
        boolean result = reservation.isBefore(LocalDateTime.of(date, time));
        // then
        assertThat(result).isEqualTo(expected);
    }
}
