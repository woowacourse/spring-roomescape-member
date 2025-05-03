package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeAlreadyExistsException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

class ReservationTimeServiceTest {
    private final LocalDate futureDate = LocalDate.now().plusDays(1);
    private final Theme theme = Theme.of(1L, "추리", "셜록 추리 게임 with Danny", "image.png");

    private ReservationService reservationService;
    private ReservationTimeService reservationTimeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
        themeRepository.put(theme);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    void createReservationTime_shouldThrowException_IfDuplicated() {
        LocalTime time = LocalTime.of(1, 1);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        reservationTimeService.create(request);

        assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(ReservationTimeAlreadyExistsException.class);
    }

    @Test
    void createReservationTime_shouldReturnResponse_WhenSuccessful() {
        LocalTime time = LocalTime.of(9, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        ReservationTimeResponse response = reservationTimeService.create(request);

        assertThat(response.startAt()).isEqualTo(time);
    }

    @Test
    void getReservationTimes_shouldReturnAllCreatedTimes() {
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservationTime_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
    }

    @Test
    void deleteReservationTime_shouldRemoveSuccessfully() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(13, 30));
        ReservationTimeResponse response = reservationTimeService.create(request);

        reservationTimeService.delete(response.id());

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();
        assertThat(result).isEmpty();
    }

    @Test
    void deleteReservationTime_shouldThrowException_WhenReservationExists() {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(
                new ReservationTimeCreateRequest(LocalTime.now()));
        reservationService.create(new ReservationCreateRequest("danny", futureDate, reservationTimeResponse.id(), theme.getId()));
        assertThatThrownBy(() -> reservationTimeService.delete(reservationTimeResponse.id()))
                .hasMessage("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    void getAvailableReservationTimes_shouldReturnAllAvailableReservationTimes() {
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(12, 0)));

        themeRepository.put(Theme.of(1L, "추리", "셜록 with Danny", "image.png"));

        reservationService.create(new ReservationCreateRequest("mint", futureDate, 1L, 1L));
        List<AvailableReservationTimeResponse> availableReservationTimes = reservationTimeService.getAvailableReservationTimes(
                futureDate, 1L);

        assertThat(
                availableReservationTimes.stream().filter(reservationTime -> !reservationTime.alreadyBooked()).count())
                .isEqualTo(2);
    }
}
