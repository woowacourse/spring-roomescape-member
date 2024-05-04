package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.service.request.ReservationRequest;

@SpringBootTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("지나간 날짜에 대한 예약은 생성할 수 없다.")
    void cantCreateReservationWithPreviousDate() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest("엘라", date.toString(), 1L, 1L);

        // when, then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("당일이지만, 이전 시간이면 예약을 생성할 수 없다.")
    void cantCreateReservationWithPreviousTime() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.now().minusHours(1)));
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest("엘라", date.toString(), reservationTime.getId(), 1L);

        // when, then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }
}
