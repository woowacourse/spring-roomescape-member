package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.controller.request.ReservationRequest;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void rejectPastTimeReservation() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().minusHours(1));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        ReservationRequest reservationRequest = new ReservationRequest(
                "비토",
                String.valueOf(LocalDate.now()),
                savedReservationTime.getId());

        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 지난 시간입니다. 입력한 시간: ");
    }
}
