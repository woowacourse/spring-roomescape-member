package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @DisplayName("같은 아이디인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1:true", "1:2:false"}, delimiter = ':')
    void isSameId(Long id1, Long id2, boolean expected) {
        Reservation reservation = new Reservation(id1, "1", LocalDate.now(), new ReservationTime(1L, LocalTime.now()));

        boolean actual = reservation.isSameId(id2);

        assertThat(actual).isEqualTo(expected);
    }
}
