package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 7, 12, 0);

    @Test
    void isInPast_과거_날짜면_true() {
        Reservation reservation = build(LocalDate.of(2026, 5, 6), LocalTime.of(12, 0));
        assertThat(reservation.isInPast(NOW)).isTrue();
    }

    @Test
    void isInPast_미래_날짜면_false() {
        Reservation reservation = build(LocalDate.of(2026, 5, 8), LocalTime.of(12, 0));
        assertThat(reservation.isInPast(NOW)).isFalse();
    }

    @Test
    void isInPast_당일_1분_전이면_true() {
        Reservation reservation = build(LocalDate.of(2026, 5, 7), LocalTime.of(11, 59));
        assertThat(reservation.isInPast(NOW)).isTrue();
    }

    @Test
    void isInPast_당일_1분_후면_false() {
        Reservation reservation = build(LocalDate.of(2026, 5, 7), LocalTime.of(12, 1));
        assertThat(reservation.isInPast(NOW)).isFalse();
    }

    @Test
    void isInPast_현재와_정확히_같은_시간이면_false() {
        Reservation reservation = build(LocalDate.of(2026, 5, 7), LocalTime.of(12, 0));
        assertThat(reservation.isInPast(NOW)).isFalse();
    }

    private Reservation build(LocalDate date, LocalTime time) {
        Theme theme = new Theme(1L, "테마", "설명", "https://thumbnail.url");
        ReservationTime reservationTime = new ReservationTime(1L, time);
        return new Reservation(null, "브라운", theme, date, reservationTime);
    }
}
