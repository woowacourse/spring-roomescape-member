package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 예약자_이름이_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2026, 5, 1), time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 예약_날짜가_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, "브라운", null, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 비어있을 수 없습니다.");
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), null, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 비어있을 수 없습니다.");
    }

    @Test
    void 예약_테마가_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 테마는 비어있을 수 없습니다.");
    }

    @Test
    void 예약자_이름과_같은_이름이면_정상통과한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.now(), time, theme);

        // when & then
        assertThatCode(() -> reservation.validateOwnedBy("어셔"))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약자_이름과_다른_이름이면_예외를_발생한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.now(), time, theme);

        // when & then
        assertThatThrownBy(() -> reservation.validateOwnedBy("레서"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);
    }

    @Test
    void 예약_시점이_주어진_시간보다_과거이면_예외를_발생한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2020, 1, 1), time, theme);

        // when & then
        assertThatThrownBy(() -> reservation.validateNotExpired(LocalDateTime.now()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_EXPIRED);

    }

    @Test
    void 예약_시점이_주어진_시각보다_미래이면_정상_통과한다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2099, 12, 31), time, theme);

        // when & then
        assertThatCode(() -> reservation.validateNotExpired(LocalDateTime.of(2026, 5, 13, 14, 0)))
                .doesNotThrowAnyException();
    }
}
