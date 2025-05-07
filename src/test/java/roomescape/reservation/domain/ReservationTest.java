package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.exception.ReservationFieldRequiredException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    @DisplayName("예약은 빈 이름으로 생성할 수 없다")
    @Test
    void reservationNameTest() {
        // given
        String name = "";
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.now().plusHours(1));
        Theme theme = Theme.createWithoutId("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time, theme))
                .isInstanceOf(ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 날짜로 생성할 수 없다")
    @Test
    void reservationDateTest() {
        // given
        String name = "슬링키";
        ReservationDate date = null;
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.now().plusHours(1));
        Theme theme = Theme.createWithoutId("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 시간으로 생성할 수 없다")
    @Test
    void reservationTimeTest() {
        // given
        String name = "슬링키";
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = null;
        Theme theme = Theme.createWithoutId("무서운 방", "무섭습니다", "/image/scary");

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }

    @DisplayName("예약은 빈 테마로 생성할 수 없다")
    @Test
    void reservationThemeTest() {
        // given
        String name = "슬링키";
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.now().plusHours(1));
        Theme theme = null;

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time, theme)).isInstanceOf(
                ReservationFieldRequiredException.class);
    }
}
