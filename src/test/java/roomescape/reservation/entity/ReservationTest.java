package roomescape.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;

class ReservationTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, delimiter = ',', nullValues = "null")
    void test1(Long id, boolean expected) {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        Reservation reservation = new Reservation(id ,"꾹이", LocalDate.now(), reservationTime);

        // when
        boolean result = reservation.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
