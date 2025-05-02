package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.reservationTime.domain.ReservationTime;

class ReservationTest {

    @ParameterizedTest
    @DisplayName("같은 시간 확인 테스트")
    @CsvSource({"20:10,true", "20:20,false", "21:10,false", "19:09,false"})
    void isSameTime_Test(LocalTime localTime, boolean expected) {
        ReservationTime reservationTime1 = ReservationTime.createWithId(1L, LocalTime.of(20, 10));
        Reservation reservation = Reservation.createWithId(1L, "a", LocalDate.of(2000, 11, 2), reservationTime1, null);

        ReservationTime reservationTime2 = ReservationTime.createWithId(2L, localTime);

        Assertions.assertThat(reservation.isSameTime(reservationTime2)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Id 할당 테스트")
    void assignId_Test() {
        Reservation withoutId = Reservation.createWithoutId("a", null, null, null);

        Reservation reservation = withoutId.assignId(1L);

        Assertions.assertThat(reservation.getId()).isEqualTo(1L);
        Assertions.assertThat(reservation.getName()).isEqualTo("a");
    }

}