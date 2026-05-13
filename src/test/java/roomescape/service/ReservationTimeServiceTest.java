package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.FakeDatabase;
import roomescape.repository.FakeReservationRepository;
import roomescape.repository.FakeReservationTimeRepository;
import roomescape.repository.FakeThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.ServiceReservationTimeRequest;
import roomescape.service.dto.ServiceReservationTimeResponse;

public class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void beforeEach() {
        FakeDatabase fakeDatabase = new FakeDatabase();

        reservationRepository = new FakeReservationRepository(fakeDatabase);
        reservationTimeRepository = new FakeReservationTimeRepository(fakeDatabase);
        themeRepository = new FakeThemeRepository(fakeDatabase);

        reservationTimeService = new ReservationTimeService(reservationTimeRepository, themeRepository,
                reservationRepository);
    }

    @Test
    void createTest() {
        ServiceReservationTimeResponse responseDto = reservationTimeService.create(
                new ServiceReservationTimeRequest(LocalTime.of(10, 0)));

        assertThat(responseDto).isEqualTo(new ServiceReservationTimeResponse(1L, LocalTime.of(10, 0)));
    }

    @Test
    void readAllTest() {
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(11, 0)));

        List<ServiceReservationTimeResponse> responseDtos = reservationTimeService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(new ServiceReservationTimeResponse(1L, LocalTime.of(10, 0)));
        assertThat(responseDtos.get(1)).isEqualTo(new ServiceReservationTimeResponse(2L, LocalTime.of(11, 0)));
    }

    @Test
    void readAvailabilityByDateAndThemeTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        reservationTimeRepository.create(new ReservationTime(LocalTime.of(11, 0)));

        Theme theme = themeRepository.create(new Theme("방탈출1", "방탈출1 설명", "url.jpg"));

        reservationRepository.create(new Reservation("fizz", LocalDate.of(2026, 5, 2), reservationTime, theme));

        List<ServiceReservationTimeAvailabilityResponse> responseDtos = reservationTimeService.readAvailabilityByDateAndTheme(
                LocalDate.of(2026, 5, 2), theme.getId());

        assertThat(responseDtos.get(0).available()).isFalse();
        assertThat(responseDtos.get(1).available()).isTrue();
    }

    @Test
    void deleteTest() {
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(10, 0)));
        reservationTimeService.delete(1L);

        List<ServiceReservationTimeResponse> responseDtos = reservationTimeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
