package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationDateTest {

    @ParameterizedTest
    @CsvSource(value = {"1,true", "0,false"})
    @DisplayName("현재 날짜보다 이전인지 확인한다.")
    void isPastDate(int day, boolean expected) {
        // given
        LocalDate date = LocalDate.now().minusDays(day);
        ReservationDate reservationDate = new ReservationDate(date);

        // when
        boolean result = reservationDate.isPastDate();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,false", "0,true"})
    @DisplayName("현재 날짜와 같은지 확인한다.")
    void isPresentDate(int day, boolean expected) {
        // given
        LocalDate date = LocalDate.now().minusDays(day);
        ReservationDate reservationDate = new ReservationDate(date);

        // when
        boolean result = reservationDate.isPresentDate();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
