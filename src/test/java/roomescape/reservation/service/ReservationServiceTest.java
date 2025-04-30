package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;

class ReservationServiceTest {

    private final LocalDate futureDate = LocalDate.now().plusDays(1);

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @Test
    void createReservation_shouldReturnResponseWhenSuccessful() {
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationTimeRepository.put(time);

        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", LocalDate.of(2025, 5, 1), 1L);
        ReservationResponse response = reservationService.create(request);

        assertThat(response.name()).isEqualTo("홍길동");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void getReservations_shouldReturnAllCreatedReservations() {
        reservationTimeRepository.put(ReservationTime.of(1L, LocalTime.of(10, 0)));
        reservationTimeRepository.put(ReservationTime.of(2L, LocalTime.of(10, 0)));
        reservationService.create(new ReservationCreateRequest("A", futureDate, 1L));
        reservationService.create(new ReservationCreateRequest("B", futureDate, 2L));

        List<ReservationResponse> result = reservationService.getReservations();
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservation_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 정보가 없습니다.");
    }

    @Test
    void deleteReservation_shouldRemoveSuccessfully() {
        reservationTimeRepository.put(ReservationTime.of(1L, LocalTime.of(9, 0)));
        ReservationResponse response = reservationService.create(
                new ReservationCreateRequest("Test", futureDate, 1L)
        );

        reservationService.delete(response.id());

        List<ReservationResponse> result = reservationService.getReservations();
        assertThat(result).isEmpty();
    }

    @Test
    void createReservation_shouldThrowException_WhenTimeIdNotFound() {
        ReservationCreateRequest request = new ReservationCreateRequest("대니", futureDate, 99L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createReservation_shouldThrowException_WhenDuplicated() {
        reservationTimeRepository.put(ReservationTime.of(1L, LocalTime.of(9, 0)));
        ReservationCreateRequest request = new ReservationCreateRequest("밍트", futureDate, 1L);
        reservationService.create(request);
        assertThatThrownBy(() -> reservationService.create(request));
    }
}