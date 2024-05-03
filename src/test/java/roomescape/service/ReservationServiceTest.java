package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.repository.ReservationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        final List<ReservationResponse> reservationResponses = reservationService.getReservations();
        assertThat(reservationResponses.size()).isEqualTo(13);
    }

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",  LocalDate.parse("2025-11-11"), 1L, 1L);
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        assertThat(reservationRepository.findById(reservationResponse.id())).isPresent();
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장")
    @Test
    void timeForSaveReservationNotFound() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", LocalDate.parse("2025-11-11"), 200L, 1L);
        assertThatThrownBy(() -> {
            reservationService.saveReservation(reservationSaveRequest);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약 저장")
    @Test
    void timeForSaveThemeNotFound() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", LocalDate.parse("2025-11-11"), 1L, 200L);
        assertThatThrownBy(() ->
            reservationService.saveReservation(reservationSaveRequest)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        reservationService.deleteReservation(1L);
        assertThat(reservationRepository.findById(1L)).isEmpty();
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(200L);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("지나간 시간 예약 저장")
    @Test
    void saveReservationWithGoneTime() {
        assertThatThrownBy(() -> {
            reservationService.saveReservation(new ReservationSaveRequest("백호", LocalDate.parse("2023-11-11"), 1L, 1L));
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("지나간 시간에 대한 예약은 생성할 수 없습니다.");
    }
}
