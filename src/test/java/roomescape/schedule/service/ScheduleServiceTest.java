package roomescape.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.AdminScheduleRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private final Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));
    
    private final Schedule schedule1 = new Schedule(LocalDateTime.of(2026, 12, 10, 12, 0), theme);
    private final Schedule schedule2 = new Schedule(LocalDateTime.of(2026, 12, 10, 16, 0), theme);

    @Autowired
    private ScheduleService scheduleService;
    
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
    @DisplayName("특정 날짜와 테마의 예약 가능한 스케줄만 조회한다.")
    void findAvailableSchedules() {
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, themeId, schedule1.getStartAt(), schedule1.getEndAt());
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, themeId, schedule2.getStartAt(), schedule2.getEndAt());
        // 1번 스케줄은 예약된 상태로 만듭니다.
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)", 1L, "test", "USER");
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 1L, 1L);

        ScheduleRequest request = new ScheduleRequest(LocalDate.of(2026, 12, 10), themeId);

        SchedulesResponse responses = scheduleService.findAvailableSchedules(request);
        List<ScheduleResponse> responseList = responses.getScheduleResponses(); 

        assertThat(responseList).isNotNull();
        assertThat(responseList).hasSize(1);
        assertThat(responseList.getFirst().getStartAt()).isEqualTo(schedule2.getStartAt());
    }

    @Test
    void 새로운_스케줄을_성공적으로_생성한다() {
        // given
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());

        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(15, 0);

        AdminScheduleRequest request = new AdminScheduleRequest(themeId, futureDate, time);

        // when
        Long createdId = scheduleService.createByAdmin(request);

        // then
        assertThat(createdId).isNotNull();

        // 생성된 스케줄을 DB에서 직접 확인하여 검증
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM schedule WHERE id = ?", Integer.class, createdId);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 이미_있는_예약_시간과_겹치는_스케줄을_생성하면_예외가_발생한다() {
        // given
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getRequiredTime());

        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime existingStartAt = LocalDateTime.of(date, LocalTime.of(14, 0));
        LocalDateTime existingEndAt = existingStartAt.plusHours(2);
        jdbcTemplate.update("INSERT INTO schedule (theme_id, start_at, end_at) VALUES (?, ?, ?)",
                themeId, existingStartAt, existingEndAt);

        LocalTime overlappingTime = LocalTime.of(15, 0);

        AdminScheduleRequest request = new AdminScheduleRequest(themeId, date, overlappingTime);

        // when & then
        assertThatThrownBy(() -> scheduleService.createByAdmin(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택하신 시간은 다른 예약 시간과 겹쳐서 추가할 수 없습니다.");
    }
}
