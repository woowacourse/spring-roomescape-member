package roomescape.date.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ReservationDateTest {

    private static final LocalDate FUTURE_DATE = LocalDate.of(2099, 1, 1);
    private static final LocalDate PAST_DATE = LocalDate.of(2000, 1, 1);

    @Test
    @DisplayName("등록할 날짜가 null이면 예외가 발생한다.")
    void create_null_date() {
        // given
        LocalDate nullDate = null;

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    ReservationDate.create(nullDate);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @Test
    @DisplayName("등록할 날짜가 과거이면 예외가 발생한다.")
    void create_before_date() {
        Assertions.assertThatThrownBy(() -> {
                    ReservationDate.create(PAST_DATE);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

}
