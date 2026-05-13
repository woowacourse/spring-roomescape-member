package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ReservationDateTest {

    @Test
    void 올바른_날짜_형식으로_생성한다() {
        ReservationDate date = ReservationDate.from("2025-06-01");

        assertThat(date.getDate().toString()).isEqualTo("2025-06-01");
    }

    @Test
    void null_날짜_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationDate.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ReservationDate.DATE_SHOULD_NOT_BE_NULL);
    }

    @Test
    void 잘못된_날짜_형식_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationDate.from("06-01-2025"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)");
    }

    @Test
    void 존재하지_않는_날짜_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationDate.from("2025-13-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)");
    }
}
