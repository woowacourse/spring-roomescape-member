package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.ResourceNotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.testutil.ReservationMemoryRepository;
import roomescape.testutil.ReservationTimeMemoryRepository;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void init() {
        final ReservationTimeRepository reservationTimeRepository = new ReservationTimeMemoryRepository();
        final ReservationRepository reservationRepository = new ReservationMemoryRepository();
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        reservationRepository.save(new Reservation("감자", "2024-05-13", new ReservationTime(1L, LocalTime.parse("10:00"))));
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        final List<ReservationResponse> reservationResponses = reservationService.getReservations();
        assertThat(reservationResponses.size()).isEqualTo(1);
    }

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", "2025-11-11", 1L);
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        final ReservationResponse expectedReservation = new ReservationResponse(2L, "고구마", "2025-11-11", new ReservationTimeResponse(1L, LocalTime.parse("10:00")));
        assertThat(reservationResponse).isEqualTo(expectedReservation);
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장")
    @Test
    void timeForSaveReservationNotFound() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", "2025-11-11", 2L);
        assertThatThrownBy(() -> {
            reservationService.saveReservation(reservationSaveRequest);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        reservationService.deleteReservation(1L);
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
}
