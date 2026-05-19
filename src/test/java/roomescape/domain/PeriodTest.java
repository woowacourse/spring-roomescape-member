package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.exception.code.BadRequestCode;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PeriodTest {

    @Test
    void 시작_날짜가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Period(null, LocalDate.of(2026, 5, 10)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(BadRequestCode.INVALID_PERIOD.getMessage());
    }

    @Test
    void 끝_날짜가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Period(LocalDate.of(2026, 5, 10), null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(BadRequestCode.INVALID_PERIOD.getMessage());
    }

    @Test
    void 끝_날짜가_시작_날짜보다_앞이면_예외가_발생한다() {
        LocalDate start = LocalDate.of(2026, 5, 10);
        LocalDate end = LocalDate.of(2026, 5, 9);

        assertThatThrownBy(() -> new Period(start, end))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(BadRequestCode.INVALID_PERIOD.getMessage());
    }

    @Test
    void 시작_날짜와_끝_날짜가_같으면_생성된다() {
        LocalDate date = LocalDate.of(2026, 5, 10);

        assertThatCode(() -> new Period(date, date))
                .doesNotThrowAnyException();
    }

    @Test
    void 끝_날짜가_시작_날짜보다_뒤이면_생성된다() {
        LocalDate start = LocalDate.of(2026, 5, 10);
        LocalDate end = LocalDate.of(2026, 5, 17);

        assertThatCode(() -> new Period(start, end))
                .doesNotThrowAnyException();
    }
}
