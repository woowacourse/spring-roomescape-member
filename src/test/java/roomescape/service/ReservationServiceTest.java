package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static roomescape.exception.ErrorCode.DUPLICATED_RESERVATION;
import static roomescape.exception.ErrorCode.NOT_FOUND_RESERVATION_TIME;
import static roomescape.exception.ErrorCode.NOT_FOUND_THEME;
import static roomescape.exception.ErrorCode.PAST_TIME_RESERVATION;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.repository.FakeDatabase;
import roomescape.repository.FakeReservationRepository;
import roomescape.repository.FakeReservationTimeRepository;
import roomescape.repository.FakeThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ServiceReservationRequest;
import roomescape.service.dto.response.ServiceReservationResponse;
import roomescape.service.dto.response.ServiceReservationTimeResponse;
import roomescape.service.dto.response.ServiceThemeResponse;

public class ReservationServiceTest {

    private ReservationService reservationService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    private LocalDateTime futureDateTime;

    @BeforeEach
    void beforeEach() {
        FakeDatabase fakeDatabase = new FakeDatabase();

        reservationRepository = new FakeReservationRepository(fakeDatabase);
        reservationTimeRepository = new FakeReservationTimeRepository(fakeDatabase);
        themeRepository = new FakeThemeRepository(fakeDatabase);

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

        futureDateTime = LocalDateTime.now().plusHours(10);
    }

    @Test
    void createNotFoundReservationTimeExceptionTest() {
        themeRepository.create(new Theme("피즈의 모험", "설명", "url.jpg"));
        ServiceReservationRequest serviceReservationRequest = new ServiceReservationRequest("fizz",
                futureDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationRequest))
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage())
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createNotFoundThemeTimeExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(futureDateTime.toLocalTime()));
        ServiceReservationRequest serviceReservationRequest = new ServiceReservationRequest("fizz",
                futureDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationRequest))
                .hasMessage(NOT_FOUND_THEME.getMessage())
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createPastReservationExceptionTest() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusHours(10);
        reservationTimeRepository.create(new ReservationTime(1L, pastDateTime.toLocalTime()));
        themeRepository.create(new Theme("피즈의 모험", "설명", "url.jpg"));

        ServiceReservationRequest serviceReservationRequest = new ServiceReservationRequest("fizz",
                pastDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationRequest))
                .hasMessage(PAST_TIME_RESERVATION.getMessage())
                .isInstanceOf(CustomException.class);
    }

    @Test
    void duplicatedReservationExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(futureDateTime.toLocalTime()));
        themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        ServiceReservationRequest requestDto = new ServiceReservationRequest("fizz", futureDateTime.toLocalDate(), 1L,
                1L);
        reservationService.create(requestDto);

        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage(DUPLICATED_RESERVATION.getMessage())
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        ServiceReservationTimeResponse serviceReservationTimeResponse = ServiceReservationTimeResponse.from(
                reservationTime);
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ServiceThemeResponse serviceThemeResponse = ServiceThemeResponse.from(theme);

        ServiceReservationResponse responseDto = reservationService.create(
                new ServiceReservationRequest("fizz", futureDateTime.toLocalDate(), 1L, 1L));

        assertThat(responseDto).isEqualTo(
                new ServiceReservationResponse(1L, "fizz", futureDateTime.toLocalDate(),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
    }

    @Test
    void readAllTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        ServiceReservationTimeResponse serviceReservationTimeResponse = ServiceReservationTimeResponse.from(
                reservationTime);
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ServiceThemeResponse serviceThemeResponse = ServiceThemeResponse.from(theme);

        reservationService.create(new ServiceReservationRequest("fizz", futureDateTime.toLocalDate(), 1L, 1L));
        reservationService.create(
                new ServiceReservationRequest("fizz2", futureDateTime.toLocalDate().plusDays(1), 1L, 1L));

        List<ServiceReservationResponse> responseDtos = reservationService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(
                new ServiceReservationResponse(responseDtos.getFirst().id(), "fizz", futureDateTime.toLocalDate(),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
        assertThat(responseDtos.get(1)).isEqualTo(
                new ServiceReservationResponse(responseDtos.get(1).id(), "fizz2",
                        futureDateTime.toLocalDate().plusDays(1),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
    }

    @Test
    void deleteTest() {
        reservationTimeRepository.create(new ReservationTime(futureDateTime.toLocalTime()));
        themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        reservationService.create(new ServiceReservationRequest("fizz", futureDateTime.toLocalDate(), 1L, 1L));
        reservationService.delete(1L);

        List<ServiceReservationResponse> responseDtos = reservationService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
