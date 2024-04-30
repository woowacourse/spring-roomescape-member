package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 저장할 수 있다.")
    void save() {
        final ReservationTime reservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        assertThat(reservationTime).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간을 조회할 수 있다.")
    void findAll() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void delete() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        reservationTimeService.delete(1L);
        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(0);
    }
}
