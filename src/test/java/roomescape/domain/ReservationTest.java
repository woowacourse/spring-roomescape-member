package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidDomainException;

class ReservationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 7, 12, 0);
    private static final Theme THEME = new Theme(1L, "테마", "설명", "https://thumbnail.url");
    private static final ReservationTime TIME = new ReservationTime(1L, LocalTime.of(12, 0));
    private static final LocalDate DATE = LocalDate.of(2026, 5, 8);

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void 예약자_이름이_비어있으면_예외(String name) {
        assertThatThrownBy(() -> new Reservation(null, name, THEME, DATE, TIME))
                .isInstanceOf(InvalidDomainException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 테마가_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", null, DATE, TIME))
                .isInstanceOf(InvalidDomainException.class)
                .hasMessage("테마는 필수입니다.");
    }

    @Test
    void 예약_날짜가_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", THEME, null, TIME))
                .isInstanceOf(InvalidDomainException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @Test
    void 예약_시간이_null이면_예외() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", THEME, DATE, null))
                .isInstanceOf(InvalidDomainException.class)
                .hasMessage("예약 시간은 필수입니다.");
    }

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
