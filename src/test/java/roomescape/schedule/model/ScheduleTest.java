package roomescape.schedule.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import roomescape.theme.model.Theme;

class ScheduleTest {

    @Test
    void 스케줄을_성공적으로_생성한다() {
        LocalDateTime startAt = LocalDateTime.of(2026,12,10,10, 0);
        LocalDateTime endAt = LocalDateTime.of(2026,12,10,12, 0);
        Theme theme = new Theme("테마", "설명", "경로");
        Boolean isAvailable = true;

        Schedule schedule = new Schedule(1L, startAt, endAt, theme);

        assertThat(schedule.getId()).isEqualTo(1L);
        assertThat(schedule.getStartAt()).isEqualTo(startAt);
        assertThat(schedule.getEndAt()).isEqualTo(endAt);
        assertThat(schedule.getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 시작_시간이_null이면_예외가_발생한다() {
        LocalDateTime startAt = null;
        LocalDateTime endAt = LocalDateTime.of(2026,12,10,12, 0);
        Theme theme = new Theme("테마", "설명", "경로");

        assertThatThrownBy(() -> new Schedule(startAt, endAt, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시작 시간은 필수입니다.");
    }

    @Test
    void ID가_없어도_시작_시간이_있으면_객체_생성에_성공한다() {
        LocalDateTime startAt = LocalDateTime.of(2026,12,10,10, 0);
        LocalDateTime endAt = LocalDateTime.of(2026,12,10,12, 0);
        Theme theme = new Theme("테마", "설명", "경로");

        Schedule schedule = new Schedule(startAt, endAt, theme);

        assertThat(schedule.getId()).isNull();
        assertThat(schedule.getStartAt()).isEqualTo(startAt);
    }
}
