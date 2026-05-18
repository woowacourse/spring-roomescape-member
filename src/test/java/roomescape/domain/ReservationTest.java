package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    @Test
    void 이름이_null이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                null,
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_비어있으면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "",
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름에_특수문자가_포함되면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브라운!",
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_두글자_미만이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브",
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_열글자_초과이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브".repeat(11),
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브라운",
                null,
                new ReservationTime(1L, LocalTime.of(10, 0)),
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약시간이_null이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브라운",
                LocalDate.now().plusDays(1),
                null,
                1L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void themeId가_null이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브라운",
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                null
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void themeId가_음수이면_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> new Reservation(
                1L,
                "브라운",
                LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                -1L
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
