package roomescape.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.constant.TestConstants.DEFAULT_THEME;
import static roomescape.constant.TestConstants.DEFAULT_TIME;

class ReservationTest {

    @Test
    void 이름이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Reservation(
                    null,
                    LocalDate.now().plusDays(1),
                    DEFAULT_TIME,
                    DEFAULT_THEME);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void 날짜가_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Reservation(
                    "두리",
                    null,
                    DEFAULT_TIME,
                    DEFAULT_THEME);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void 시간이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Reservation(
                    "두리",
                    LocalDate.of(2025, 10, 5),
                    null,
                    DEFAULT_THEME
            );
        }).isInstanceOf(NullPointerException.class);
    }
}
