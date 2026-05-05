package roomescape.reservation.model;

import org.junit.jupiter.api.Test;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0),
            LocalDateTime.of(2026, 12, 10, 14, 0), theme);

    @Test
    void 올바른_정보로_예약을_생성하면_예외가_발생하지_않는다() {
        assertThatCode(() -> new Reservation(user, schedule, theme))
                .doesNotThrowAnyException();
    }

    @Test
    void 스케줄이나_테마_정보가_누락되면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(user, null, theme))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Reservation(user, schedule, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
