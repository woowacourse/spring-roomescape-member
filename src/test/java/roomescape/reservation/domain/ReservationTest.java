package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("예약 도메인 테스트")
class ReservationTest {
    @DisplayName("동일한 id는 같은 예약이다.")
    @Test
    void equals() {
        //given
        long id = 1L;
        String name1 = "choco";
        LocalDate date1 = LocalDate.of(2024, 4, 19);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time1 = new ReservationTime(timeId, localTime);

        String name2 = "pororo";
        LocalDate date2 = LocalDate.of(2022, 4, 19);
        LocalTime time2 = LocalTime.of(11, 23, 0);

        //when
        Reservation reservation1 = new Reservation(id, name1, date1, time1);
        Reservation reservation2 = new Reservation(id, name2, date2, time1);

        //then
        assertThat(reservation1).isEqualTo(reservation2);
    }
}
