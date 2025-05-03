package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.DomainFixtures;

public class ReservationTest {

    @Test
    @DisplayName("이름이 여섯 글자 이상이면 예외가 발생한다")
    void nameLengthException() {
        assertThatThrownBy(() -> new Reservation(
            1L,
            "여섯글자이름",
            LocalDate.of(2023, 12, 1),
            new TimeSlot(1L, LocalTime.of(10, 0)),
            DomainFixtures.JUNK_THEME)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 일시가 주어진 일시보다 이전인지 확인한다")
    void isBefore() {
        // given
        var reserveDate = LocalDate.of(2023, 12, 1);
        var reserveTime = LocalTime.of(10, 0);
        var compareDateTime = LocalDateTime.of(reserveDate, reserveTime).plusDays(1);
        var reservation = new Reservation(1L, "리버", reserveDate,
            new TimeSlot(1L, reserveTime), DomainFixtures.JUNK_THEME);

        // when
        boolean isBefore = reservation.isBefore(compareDateTime);

        // then
        assertThat(isBefore).isTrue();
    }

    @Test
    @DisplayName("예약 일시가 주어진 예약과 같은 일시인지 확인한다")
    void isSameDateTime() {
        // given
        var reserveDate = LocalDate.of(2023, 12, 1);
        var reserveTime = LocalTime.of(10, 0);
        var reservation = new Reservation(1L, "리버", reserveDate,
            new TimeSlot(1L, reserveTime), DomainFixtures.JUNK_THEME);
        var otherReservation = new Reservation(2L, "포포", reserveDate,
            new TimeSlot(1L, reserveTime), DomainFixtures.JUNK_THEME);

        // when
        boolean isSameDateTime = reservation.isSameDateTime(otherReservation);

        // then
        assertThat(isSameDateTime).isTrue();
    }
}
