package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        LocalDate date1 = LocalDate.now().plusYears(1);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time1 = new ReservationTime(timeId, localTime);

        LocalDate date2 = LocalDate.now().plusYears(1);
        LocalTime time2 = LocalTime.of(11, 23, 0);

        long themeId = 1;
        Theme theme1 = new Theme(themeId, "name", "description", "thumbnail");
        Theme theme2 = new Theme(themeId, "name", "description", "thumbnail");

        //when
        Reservation reservation1 = new Reservation(id, date1, time1, theme1);
        Reservation reservation2 = new Reservation(id, date2, time1, theme2);

        //then
        assertThat(reservation1).isEqualTo(reservation2);
    }

    @DisplayName("현재보다 이전 시각의 예약을 생성하면 예외가 발생한다.")
    @Test
    void createIllegalException() {
        //given
        LocalDate localDate = LocalDate.of(2017, 12, 30);
        long timeId = 1;
        LocalTime localTime = LocalTime.of(12, 23, 0);
        ReservationTime time = new ReservationTime(timeId, localTime);

        long themeId = 1L;
        Theme theme = new Theme(themeId, "name", "description", "thumbnail");

        //when & then
        assertThatThrownBy(() -> new Reservation(1L, localDate, time, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
