package roomescape.schedule.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.ScheduleResponse;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private final Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));
    
    private final Schedule schedule1 = new Schedule(LocalDateTime.of(2026, 12, 10, 12, 0),
            LocalDateTime.of(2026, 12, 10, 14, 0), theme);
    private final Schedule schedule2 = new Schedule(LocalDateTime.of(2026, 12, 10, 16, 0),
            LocalDateTime.of(2026, 12, 10, 18, 0), theme);

    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 데이터베이스에_저장된_모든_스케줄을_조회한다() {
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, themeId, schedule1.getStartAt(), schedule1.getEndAt());
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, themeId, schedule2.getStartAt(), schedule2.getEndAt());

        ScheduleRequest request = new ScheduleRequest(LocalDate.of(2026, 12, 10), themeId);

        SchedulesResponse responses = scheduleService.findAll(request);
        List<ScheduleResponse> responseList = responses.getScheduleResponses(); 

        assertThat(responseList).isNotNull();
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getStartAt()).isEqualTo(schedule1.getStartAt());
        assertThat(responseList.get(1).getStartAt()).isEqualTo(schedule2.getStartAt());
    }
}
