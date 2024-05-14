package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationDateTest {

    @ParameterizedTest
    @CsvSource(value = {"1,true", "0,false"})
    @DisplayName("예약 가능한 기준 날짜보다 이전인지 확인한다.")
    void isPastDate(int minusDay, boolean expected) {
        // given
        LocalDate date = LocalDate.now().minusDays(minusDay);
        ReservationDate reservationDate = new ReservationDate(date);
        LocalDate limitDate = LocalDate.now();

        // when
        boolean result = reservationDate.isBefore(limitDate);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,false", "0,true"})
    @DisplayName("예약 가능한 기준 날짜와 같은지 확인한다.")
    void isPresentDate(int minusDay, boolean expected) {
        // given
        LocalDate date = LocalDate.now().minusDays(minusDay);
        ReservationDate reservationDate = new ReservationDate(date);
        LocalDate limitDate = LocalDate.now();

        // when
        boolean result = reservationDate.isLimitDate(limitDate);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
