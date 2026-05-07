package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationLocalDateTest {

    @Test
    void 오늘_이후_날짜로_생성한다() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // when
        ReservationLocalDate date = ReservationLocalDate.createForSave(tomorrow);

        // then
        assertThat(date.value()).isEqualTo(tomorrow);
    }

    @Test
    void 오늘_날짜로_생성하면_예외가_발생한다() {
        // given
        LocalDate today = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> ReservationLocalDate.createForSave(today))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("오늘 이후의 날짜만 선택할 수 있습니다.");
    }

    @Test
    void 과거_날짜로_생성하면_예외가_발생한다() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> ReservationLocalDate.createForSave(yesterday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("오늘 이후의 날짜만 선택할 수 있습니다.");
    }

}