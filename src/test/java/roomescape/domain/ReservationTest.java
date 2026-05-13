package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private static final ReservationTime TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final Theme THEME = new Theme(1L, "우주 정거장", "설명", "https://example.com/1.jpg");
    private static final LocalDate DATE = LocalDate.of(2026, 5, 10);

    @Test
    void 이름이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, null, DATE, TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_RESERVATION_NAME.getMessage());
    }

    @Test
    void 이름이_공백이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, " ", DATE, TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.BLANK_RESERVATION_NAME.getMessage());
    }

    @Test
    void 날짜가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", null, TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_RESERVATION_DATE.getMessage());
    }

    @Test
    void 시간이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", DATE, null, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_RESERVATION_TIME.getMessage());
    }

    @Test
    void 테마가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", DATE, TIME, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_RESERVATION_THEME.getMessage());
    }
}
