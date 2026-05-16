package roomescape.schedule.model;

import org.junit.jupiter.api.Test;
import roomescape.theme.model.Theme;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleTest {

    private final Theme theme = new Theme(1L, "우테코 방탈출", "꿀잼", "path", LocalTime.of(1, 30));

    @Test
    void 스케줄_시작_시간이_현재_시간보다_이전이면_true를_반환한다() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2026, 5, 16, 14, 59);
        Schedule schedule = new Schedule(startAt, theme);

        LocalDateTime targetTime = LocalDateTime.of(2026, 5, 16, 15, 0);

        // when
        boolean result = schedule.isBefore(targetTime);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 스케줄_시작_시간이_현재_시간이랑_같으면_false를_반환한다() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2026, 5, 16, 15, 0);
        Schedule schedule = new Schedule(startAt, theme);

        LocalDateTime targetTime = LocalDateTime.of(2026, 5, 16, 15, 0);

        // when
        boolean result = schedule.isBefore(targetTime);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 스케줄_시작_시간이_현재_시간보다_이후이면_false를_반환한다() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2026, 5, 16, 16, 0);
        Schedule schedule = new Schedule(startAt, theme);

        LocalDateTime targetTime = LocalDateTime.of(2026, 5, 16, 15, 59);

        // when
        boolean result = schedule.isBefore(targetTime);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 스케줄_생성_시_시작_시간이_null이면_IllegalArgumentException이_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Schedule(null, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스케줄 시작 시간은 필수입니다.");
    }

    @Test
    void 스케줄_생성_시_테마_정보가_null이면_IllegalArgumentException이_발생한다() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2026, 5, 16, 12, 0);

        // when & then
        assertThatThrownBy(() -> new Schedule(startAt, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 정보는 필수입니다.");
    }
}
