package roomescape.schedule.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.support.DatabaseHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ScheduleRepositoryTest {

    private final Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(LocalDateTime.of(2026, 12, 10, 12, 0), theme);

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DatabaseHelper databaseHelper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        databaseHelper.cleanUp();
    }

    @Test
    void 예약된_스케줄을_데이터베이스에서_정상적으로_조회한다() {
        Long themeId = 1L;
        databaseHelper.insertTheme(themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), "02:00:00");
        databaseHelper.insertSchedule(1L, themeId, "2026-12-10 12:00:00", "2026-12-10 14:00:00");

        List<Schedule> schedules = scheduleRepository.findAll(themeId, LocalDate.of(2026, 12, 10));

        assertThat(schedules).isNotNull();
        assertThat(schedules.size()).isEqualTo(1);
        assertThat(schedules.getFirst().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(schedules.getFirst().getEndAt()).isEqualTo(schedule.getEndAt());
        assertThat(schedules.getFirst().getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 스케줄을_데이터베이스에서_정상적으로_삭제한다() {
        databaseHelper.insertTheme(50L, theme.getName(), theme.getDescription(), theme.getImageUrl(), "02:00:00");
        databaseHelper.insertSchedule(50L, 50L, "2026-05-25 14:00:00", "2026-05-25 16:00:00");

        scheduleRepository.delete(50L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM schedule WHERE id=?", Integer.class, 50L);
        assertThat(count).isEqualTo(0);
    }
}
