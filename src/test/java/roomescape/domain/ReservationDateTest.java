package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationDateTest {

    @DisplayName("date가 null이면 ReservationDate생성 시 예외가 발생한다")
    @Test
    void should_throw_NPE_when_date_is_null() {
        assertThatThrownBy(() -> new ReservationDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("날짜는 null일 수 없습니다");
    }

    @DisplayName("date가 현재 날짜 보다 이전 날짜이면 ReservationDate생성 시 예외가 발생한다")
    @Test
    void should_throw_IllegalArgument_when_date_is_past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> new ReservationDate(pastDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(pastDate + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
    }
}
