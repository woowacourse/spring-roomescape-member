package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static roomescape.DomainFixtures.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.DateUtils;

public class ReservationTest {

    @Test
    @DisplayName("이름이 여섯 글자 이상이면 예외가 발생한다")
    void nameLengthException() {
        assertThatThrownBy(() -> Reservation.of(
            1L,
            "여섯글자이름",
            LocalDate.of(2023, 12, 1),
            new TimeSlot(1L, LocalTime.of(10, 0)),
            JUNK_THEME)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 일시가 현재 일시보다 이전이면 예외가 발생한다.")
    void isBefore() {
        // given
        var name = "포포";
        var yesterday = DateUtils.yesterday();
        var timeSlot = JUNK_TIME_SLOT;
        var theme = JUNK_THEME;

        // when & then
        assertThatThrownBy(() -> Reservation.reserveNewly(name, yesterday, timeSlot, theme))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 날짜가 주어진 날짜와 같은 지 확인한다")
    void isDateEquals() {
        // given
        var date = LocalDate.of(2023, 12, 1);
        var reservation = Reservation.of(1L, "리버", date, JUNK_TIME_SLOT, JUNK_THEME);

        // when
        boolean dateEquals = reservation.isDateEquals(LocalDate.of(2023, 12, 1));

        // then
        assertThat(dateEquals).isTrue();
    }

    @Test
    @DisplayName("예약 날짜가 주어진 날짜와 같은 지 확인한다")
    void isTimeSlotEquals() {
        // given
        var date = LocalDate.of(2023, 12, 1);
        var timeSlot = new TimeSlot(1L, LocalTime.of(10, 0));
        var reservation = Reservation.of(1L, "리버", date, timeSlot, JUNK_THEME);

        // when
        boolean timeSlotEquals = reservation.isTimeSlotEquals(new TimeSlot(1L, LocalTime.of(10, 0)));

        // then
        assertThat(timeSlotEquals).isTrue();
    }
}
