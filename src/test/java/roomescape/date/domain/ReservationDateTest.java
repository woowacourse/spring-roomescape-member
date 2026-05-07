package roomescape.date.domain;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationDateTest {
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
        // given
        LocalDate pastDate = LocalDate.of(2000, 1, 1);

        // when & then
        Assertions.assertThatThrownBy(() -> ReservationDate.create(pastDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 날짜는 등록할 수 없습니다.");
    }
}
