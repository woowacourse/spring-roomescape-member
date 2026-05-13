package roomescape.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.TimeAvailability;

@JdbcTest
@Import(ReservationTimeDao.class)
public class ReservationTimeDaoTest {
    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void 예약_시간_조회_테스트() {

        Long themeId = 2L;
        LocalDate date = LocalDate.parse("2026-05-05");
        List<TimeAvailability> availableTimes = reservationTimeDao.selectByThemeIdAndDate(themeId, date);

        assertThat(availableTimes.size()).isEqualTo(11);
        assertThat(availableTimes.getFirst().available()).isFalse();
    }
}
