package roomescape.schedule.repository;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM \"USER\"");
    }

    @Test
    void 특정_날짜와_테마의_모든_스케줄을_조회한다() {
        // given
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());
        jdbcTemplate.update("INSERT INTO schedule (theme_id, start_at, end_at) VALUES (?, ?, ?)",
                themeId, schedule.getStartAt(), schedule.getEndAt());

        // when
        List<Schedule> schedules = scheduleRepository.findAllByThemeIdAndDate(themeId, LocalDate.of(2026, 12, 10));

        // then
        assertThat(schedules).isNotNull();
        assertThat(schedules.size()).isEqualTo(1);
        assertThat(schedules.getFirst().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(schedules.getFirst().getEndAt()).isEqualTo(schedule.getEndAt());
        assertThat(schedules.getFirst().getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 특정_날짜와_테마의_예약_가능한_스케줄만_조회한다() {
        // given
        Long themeId = 1L;
        Long userId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, "테마", "설명", "경로", LocalTime.of(2, 0));
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)",
                userId, "유저", "USER");

        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, themeId, "2026-12-10 10:00:00", "2026-12-10 12:00:00");
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)",
                1L, userId);

        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, themeId, "2026-12-10 13:00:00", "2026-12-10 15:00:00");

        // when
        List<Schedule> availableSchedules = scheduleRepository.findAllAvailableByThemeAndDate(themeId, LocalDate.of(2026, 12, 10));

        // then
        assertThat(availableSchedules).hasSize(1);
        assertThat(availableSchedules.getFirst().getId()).isEqualTo(2L);
    }
}
