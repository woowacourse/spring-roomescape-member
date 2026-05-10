package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 생성 성공")
    void 예약_생성_성공() {
        ReservationResponse response = reservationService.createReservation(
                new ReservationRequest("현미밥", LocalDate.now().plusDays(1), 1L, 1L));
        assertThat(response.id()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 timeId로 예약 생성 시 예외 발생")
    void 존재하지_않는_timeId_예외() {
        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("현미밥", LocalDate.now().plusDays(1), 999L, 1L)))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("존재하지 않는 themeId로 예약 생성 시 예외 발생")
    void 존재하지_않는_themeId_예외() {
        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("현미밥", LocalDate.now().plusDays(1), 1L, 999L)))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("예약 삭제 후 해당 시간 예약 가능")
    void 예약_삭제_성공() {
        assertThat(reservationTimeService.getAvailableTimes(LocalDate.of(2026, 5, 10), 1L)).hasSize(2);

        reservationService.deleteReservation(1L);

        assertThat(reservationTimeService.getAvailableTimes(LocalDate.of(2026, 5, 10), 1L)).hasSize(3);
    }
}
