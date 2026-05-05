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
    void 시작_시간이_null이면_예외가_발생한다() {
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        assertThatThrownBy(() -> new Schedule(null, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시작 시간은 필수입니다.");
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
    void 오전_10시_이전에_예약을_하면_예외가_발생한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 9, 59);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        assertThatThrownBy(() -> new Schedule(startAt, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("오전 10시 이전에는 예약이 불가능합니다.");
    }

    @Test
    void 오후_8시_이후에_종료되는_스케줄이면_예외가_발생한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 18, 1);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        assertThatThrownBy(() -> new Schedule(startAt, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("오후 8시 이후에는 예약이 불가능합니다.");
    }

    @Test
    void 오후_8시_정각에_종료되는_스케줄은_성공한다() {
        LocalDateTime startAt = LocalDateTime.of(2026, 12, 10, 18, 0);
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Schedule schedule = new Schedule(startAt, theme);

        assertThat(schedule.getEndAt().toLocalTime()).isEqualTo(LocalTime.of(20, 0));
    }
}
