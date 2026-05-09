package roomescape.domain.vo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReservationDateTest {
    @Test
    void 오늘_이전의_날짜로_저장시_예외가_발생한다() {
        // when & then
        assertThatCode(() -> ReservationDate.createForSave(LocalDate.now().minusDays(2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("오늘 이후의 날짜만 선택할 수 있습니다.");
    }
}