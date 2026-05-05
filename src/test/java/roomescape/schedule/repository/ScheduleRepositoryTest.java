package roomescape.schedule.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;

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
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약된_스케줄을_데이터베이스에서_정상적으로_조회한다() {
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());
        jdbcTemplate.update("INSERT INTO schedule (theme_id, start_at, end_at) VALUES (?, ?, ?)",
                themeId, schedule.getStartAt(), schedule.getEndAt());

        List<Schedule> schedules = scheduleRepository.findAll(themeId, LocalDate.of(2026, 12, 10));

        assertThat(schedules).isNotNull();
        assertThat(schedules.size()).isEqualTo(1);
        assertThat(schedules.getFirst().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(schedules.getFirst().getEndAt()).isEqualTo(schedule.getEndAt());
        assertThat(schedules.getFirst().getTheme().getName()).isEqualTo(theme.getName());
    }
}
