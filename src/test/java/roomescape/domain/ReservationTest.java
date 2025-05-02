package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.reservation.ReservationFieldRequiredException;
import roomescape.exception.reservation.ReservationInPastException;

class ReservationTest {

    @DisplayName("예약은 빈 이름으로 생성할 수 없다")
    @Test
    void reservationNameTest() {
        // given
        String name = "";
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        Theme theme = new Theme("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 날짜로 생성할 수 없다")
    @Test
    void reservationDateTest() {
        // given
        String name = "슬링키";
        LocalDate date = null;
        ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        Theme theme = new Theme("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 시간으로 생성할 수 없다")
    @Test
    void reservationTimeTest() {
        // given
        String name = "슬링키";
        LocalDate date = LocalDate.now();
        ReservationTime time = null;
        Theme theme = new Theme("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 테마로 생성할 수 없다")
    @Test
    void reservationThemeTest() {
        // given
        String name = "슬링키";
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        Theme theme = null;

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 이미 지난 시간으로 할 수 없다")
    @Test
    void reservationInPastTest() {
        // given
        String name = "슬링키";
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(LocalTime.now().minusMinutes(1));
        Theme theme = null;

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(ReservationInPastException.class);
    }
}
