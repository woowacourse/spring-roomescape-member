package roomescape.support;

import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestFixture {
    public static final User USER_1 =
            new User(1L, "user1", Role.USER);
    public static final Theme THEME_공포 =
            new Theme(1L, "공포", "설명", "경로",
                    LocalTime.of(2, 0));
    public static final Schedule SCHEDULE_12시 =
            new Schedule(1L, LocalDateTime.of(2026, 12,
                    10, 12, 0), THEME_공포);
}