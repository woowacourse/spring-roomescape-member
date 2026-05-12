package roomescape.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.ScheduleResponse;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.support.DatabaseHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private final Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

    private final Schedule schedule1 = new Schedule(LocalDateTime.of(2026, 12, 10, 12, 0), theme);
    private final Schedule schedule2 = new Schedule(LocalDateTime.of(2026, 12, 10, 16, 0), theme);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DatabaseHelper databaseHelper;

    @BeforeEach
    void setUp() {
        databaseHelper.cleanUp();
    }

    @Test
    void 데이터베이스에_저장된_모든_스케줄을_조회한다() {
        Long themeId = 1L;
        databaseHelper.insertTheme(themeId, theme.getName(), theme.getDescription(), theme.getImageUrl(), "02:00:00");
        databaseHelper.insertSchedule(1L, themeId, "2026-12-10 12:00:00", "2026-12-10 14:00:00");
        databaseHelper.insertSchedule(2L, themeId, "2026-12-10 16:00:00", "2026-12-10 18:00:00");

        ScheduleRequest request = new ScheduleRequest(LocalDate.of(2026, 12, 10), themeId);

        SchedulesResponse responses = scheduleService.findAll(request);
        List<ScheduleResponse> responseList = responses.getScheduleResponses();

        assertThat(responseList).isNotNull();
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getStartAt()).isEqualTo(schedule1.getStartAt());
        assertThat(responseList.get(1).getStartAt()).isEqualTo(schedule2.getStartAt());
    }

    @Test
    void 예약된_스케줄을_삭제하려면_에러가_발생한다() {
        databaseHelper.insertUser(51L, "user", "USER");
        databaseHelper.insertTheme(51L, "테마", "설명", "경로", "02:00:00");
        databaseHelper.insertSchedule(51L, 51L, "2026-10-10 12:00:00", "2026-10-10 14:00:00");
        databaseHelper.insertReservation(51L, 51L, 51L);

        assertThatThrownBy(() -> scheduleService.delete(51L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하는 스케줄은 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_스케줄을_정상적으로_삭제한다() {
        databaseHelper.insertTheme(52L, "테마", "설명", "경로", "02:00:00");
        databaseHelper.insertSchedule(52L, 52L, "2026-10-10 12:00:00", "2026-10-10 14:00:00");

        assertDoesNotThrow(() -> scheduleService.delete(52L));
    }
}
