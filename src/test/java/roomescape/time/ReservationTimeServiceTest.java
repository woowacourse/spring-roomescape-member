package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.dto.ReservationTimeResponse;

@SpringBootTest
@Transactional
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_가능한_시간_조회() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 05, 10);

        List<ReservationTimeResponse> availableTimes = reservationTimeService.readAvailableTimes(themeId, date);

        List<ReservationTimeResponse> expected = List.of(
                new ReservationTimeResponse(2L, LocalTime.of(11, 00)),
                new ReservationTimeResponse(4L, LocalTime.of(16, 00))
        );

        assertThat(availableTimes).isEqualTo(expected);
    }

    @Test
    void 예약_가능한_시간_없는_경우() {
        Long themeId = 5L;
        LocalDate date = LocalDate.of(2026, 05, 10);

        List<ReservationTimeResponse> availableTimes = reservationTimeService.readAvailableTimes(themeId, date);

        List<ReservationTimeResponse> expected = List.of();

        assertThat(availableTimes).isEqualTo(expected);
    }
}
