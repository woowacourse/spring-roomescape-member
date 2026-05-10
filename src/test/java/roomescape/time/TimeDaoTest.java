package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.time.dto.AvailableTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeDaoTest {
    @Autowired
    private TimeDao timeDao;

    @Test
    void 예약_시간_조회_테스트() {

        Long themeId = 2L;
        LocalDate date = LocalDate.parse("2026-05-05");
        List<AvailableTime> availableTimes = timeDao.selectByThemeIdAndDate(themeId, date);

        assertThat(availableTimes.size()).isEqualTo(11);
        assertThat(availableTimes.getFirst().getIsAvailable()).isFalse();
    }
}
