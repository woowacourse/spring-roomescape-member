package roomescape.reservation.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.DummyDataFixture;

class ReservationTest extends DummyDataFixture {

    @Test
    @DisplayName("주어진 날짜와 시간보다 예약의 날짜와 시간이 이전인 경우 참을 반환한다.")
    void isBeforeDateTimeThanNow() {
        Reservation reservation = new Reservation(
                1L,
                "아서",
                LocalDate.parse("2024-04-23"),
                new ReservationTime(null, LocalTime.parse("10:00")),
                super.getThemeById(1L));
        assertTrue(reservation.isBeforeDateTimeThanNow(LocalDateTime.parse("2024-05-23T10:15:30")));
    }

    @Test
    @DisplayName("주어진 날짜와 시간보다 예약의 날짜가 이후인 경우 거짓을 반환한다.")
    void isBeforeDateTimeThanNow_WhenDataIsAfter() {
        LocalTime sameTime = LocalTime.parse("10:00");
        Reservation reservation = new Reservation(
                1L,
                "아서",
                LocalDate.parse("2024-04-23"),
                new ReservationTime(null, sameTime),
                super.getThemeById(1L));
        assertFalse(reservation.isBeforeDateTimeThanNow(LocalDateTime.of(LocalDate.parse("2024-01-23"), sameTime)));
    }

    @Test
    @DisplayName("주어진 날짜와 시간보다 예약의 시간이 이후인 경우 거짓을 반환한다.")
    void isBeforeDateTimeThanNow_WhenTimeIsAfter() {
        LocalDate sameDate = LocalDate.parse("2024-04-23");
        Reservation reservation = new Reservation(
                1L,
                "아서",
                sameDate,
                new ReservationTime(null, LocalTime.parse("10:00")),
                super.getThemeById(1L));
        assertFalse(reservation.isBeforeDateTimeThanNow(LocalDateTime.of(sameDate, LocalTime.parse("09:00"))));
    }

    @Test
    @DisplayName("주어진 날짜와 시간보다 예약의 날짜와 시간이 동일한 경우 거짓을 반환한다.")
    void isBeforeDateTimeThanNow_WhenDataTimeIsSame() {
        LocalDate sameDate = LocalDate.parse("2024-04-23");
        LocalTime sameTime = LocalTime.parse("10:00");
        Reservation reservation = new Reservation(
                1L,
                "아서",
                sameDate,
                new ReservationTime(null, sameTime),
                super.getThemeById(1L));
        assertFalse(reservation.isBeforeDateTimeThanNow(LocalDateTime.of(sameDate, sameTime)));
    }
}
