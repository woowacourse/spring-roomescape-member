package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;
import roomescape.global.exception.DomainNotValidValueException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 유효한_값으로_예약을_생성하면_필드가_저장된다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @Test
    void 예약자_이름이_빈_문자열이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, "", LocalDate.of(2026, 5, 1), time, theme))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 예약자_이름이_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2026, 5, 1), time, theme))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 예약_날짜가_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, "브라운", null, time, theme))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("예약 날짜는 비어있을 수 없습니다.");
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), null, theme))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("예약 시간은 비어있을 수 없습니다.");
    }

    @Test
    void 예약_테마가_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time, null))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("예약 테마는 비어있을 수 없습니다.");
    }

    @Test
    void 예약자_이름과_같은_이름이면_true를_반환한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.now(), time, theme);

        // when & then
        assertThat(reservation.isOwnedBy("어셔")).isTrue();
    }

    @Test
    void 예약자_이름과_다른_이름이면_false를_반환한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.now(), time, theme);

        // when & then
        assertThat(reservation.isOwnedBy("어셔2")).isFalse();
    }

    @Test
    void 예약_시점이_주어진_시간보다_과거이면_true를_반환한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2020, 1, 1), time, theme);

        // when & then
        assertThat(reservation.isExpired(LocalDateTime.of(2026, 5, 13, 14, 0))).isTrue();
    }

    @Test
    void 예약_시점이_주어진_시각보다_미래이면_false를_반환한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2099, 12, 31), time, theme);

        // when & then
        assertThat(reservation.isExpired(LocalDateTime.of(2026, 5, 13, 14, 0))).isFalse();
    }
}
