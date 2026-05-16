package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 종료_시간은_시작_시간보다_늦어야_한다() {
        assertThatThrownBy(() -> new ReservationTime(LocalTime.of(9, 0), LocalTime.of(8, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 종료 시간은 시작 시간보다 늦어야 합니다.");
    }

    @Test
    void 종료_시간과_시작_시간이_같으면_예외가_발생한다() {
        assertThatThrownBy(() -> new ReservationTime(LocalTime.of(9, 0), LocalTime.of(9, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 종료 시간은 시작 시간보다 늦어야 합니다.");
    }
}
