package roomescape.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Schedule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScheduleDaoTest {

    @Autowired
    private ScheduleDao scheduleDao;

    @Test
    void 예약_시간_조회_테스트() {

        Long themeId = 2L;
        LocalDate date = LocalDate.parse("2026-05-05");
        List<Schedule> schedules = scheduleDao.selectById(themeId, date);

        assertThat(schedules.size()).isEqualTo(11);
    }
}
