package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 올바른_시간_형식으로_생성한다() {
        ReservationTime time = ReservationTime.of("10:00");

        assertThat(time.getStartAt().toString()).isEqualTo("10:00");
    }

    @Test
    void null_시간_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationTime.of((String) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ReservationTime.TIME_SHOULD_NOT_BE_NULL);
    }

    @Test
    void 잘못된_시간_형식_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationTime.of("25:00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간 형식이 올바르지 않습니다. (HH:mm)");
    }

    @Test
    void id와_문자열로_생성시_잘못된_형식이면_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationTime.of(1L, "abc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간 형식이 올바르지 않습니다. (HH:mm)");
    }
}
