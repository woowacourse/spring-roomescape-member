package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

class ReservationServiceTest {

    private final LocalDate futureDate = LocalDate.now().plusDays(1);

    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        ThemeRepository themeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationTimeRepository.save(time);
        Theme theme = Theme.of(1L, "추리", "셜록 추리 게임 with Danny", "image.png");
        themeRepository.put(theme);
    }

    @Test
    void createReservation_shouldReturnResponseWhenSuccessful() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", futureDate, 1L, 1L);
        ReservationResponse response = reservationService.create(request);

        assertThat(response.name()).isEqualTo("홍길동");
        assertThat(response.date()).isEqualTo(futureDate);
        assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void getReservations_shouldReturnAllCreatedReservations() {
        reservationTimeRepository.save(ReservationTime.of(2L, LocalTime.of(10, 0)));
        reservationService.create(new ReservationCreateRequest("A", futureDate, 1L, 1L));
        reservationService.create(new ReservationCreateRequest("B", futureDate, 2L, 1L));

        List<ReservationResponse> result = reservationService.getReservations();
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservation_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 정보가 없습니다.");
    }

    @Test
    void deleteReservation_shouldRemoveSuccessfully() {
        ReservationResponse response = reservationService.create(
                new ReservationCreateRequest("Test", futureDate, 1L, 1L)
        );

        reservationService.delete(response.id());

        List<ReservationResponse> result = reservationService.getReservations();
        assertThat(result).isEmpty();
    }

    @Test
    void createReservation_shouldThrowException_WhenTimeIdNotFound() {
        ReservationCreateRequest request = new ReservationCreateRequest("대니", futureDate, 99L, 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void createReservation_shouldThrowException_WhenDuplicated() {
        ReservationCreateRequest request = new ReservationCreateRequest("밍트", futureDate, 1L, 1L);
        reservationService.create(request);
        assertThatThrownBy(() -> reservationService.create(request));
    }
}
