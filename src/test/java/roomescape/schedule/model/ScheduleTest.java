package roomescape.schedule.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import roomescape.theme.model.Theme;

class ScheduleTest {

    @Test
    void 스케줄을_성공적으로_생성한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 10, 0);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Schedule schedule = new Schedule(1L, startAt, theme);

        assertThat(schedule.getId()).isEqualTo(1L);
        assertThat(schedule.getStartAt()).isEqualTo(startAt);
        assertThat(schedule.getEndAt()).isEqualTo(LocalDateTime.of(2026, 12, 10, 12, 0));
        assertThat(schedule.getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 테마가_null이면_예외가_발생한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 10, 0);

        assertThatThrownBy(() -> new Schedule(startAt, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 정보는 필수입니다.");
    }

    @Test
    void ID가_없어도_생성에_성공한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 10, 0);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Schedule schedule = new Schedule(startAt, theme);

        assertThat(schedule.getId()).isNull();
    }

    @Test
    void 오전_10시_정각_예약은_성공한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 10, 0);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Schedule schedule = new Schedule(startAt, theme);

        assertThat(schedule.getStartAt().toLocalTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(schedule.getEndAt().toLocalTime()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    void 오후_8시_정각에_종료되는_스케줄은_성공한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 18, 0);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Schedule schedule = new Schedule(startAt, theme);

        assertThat(schedule.getEndAt().toLocalTime()).isEqualTo(LocalTime.of(20, 0));
    }
}
