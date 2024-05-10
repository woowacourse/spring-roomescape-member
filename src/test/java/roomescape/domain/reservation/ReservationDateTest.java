package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationDateTest {

    @ParameterizedTest
    @CsvSource(value = {"2024-04-04,2024-04-03,true", "2024-04-04,2024-05-05,false", "2024-04-04,2024-04-04,false"})
    @DisplayName("비교 날짜보다 이후인지 판단한다.")
    void isAfter(String from, String to, boolean expected) {
        //given
        ReservationDate fromDate = ReservationDate.from(from);
        ReservationDate toDate = ReservationDate.from(to);

        //when
        boolean result = fromDate.isAfter(toDate);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
