package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static roomescape.exception.ErrorCode.DUPLICATED_RESERVATION;
import static roomescape.exception.ErrorCode.NOT_FOUND_RESERVATION;
import static roomescape.exception.ErrorCode.NOT_FOUND_RESERVATION_TIME;
import static roomescape.exception.ErrorCode.NOT_FOUND_THEME;
import static roomescape.exception.ErrorCode.PAST_TIME_RESERVATION;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
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
import roomescape.service.dto.request.ServiceReservationCreateRequest;
import roomescape.service.dto.request.ServiceReservationUpdateRequest;
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
        ServiceReservationCreateRequest serviceReservationCreateRequest = new ServiceReservationCreateRequest("fizz",
                futureDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationCreateRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage());
    }

    @Test
    void createNotFoundThemeTimeExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(futureDateTime.toLocalTime()));
        ServiceReservationCreateRequest serviceReservationCreateRequest = new ServiceReservationCreateRequest("fizz",
                futureDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationCreateRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_THEME.getMessage());
    }

    @Test
    void createPastReservationExceptionTest() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusHours(10);
        reservationTimeRepository.create(new ReservationTime(1L, pastDateTime.toLocalTime()));
        themeRepository.create(new Theme("피즈의 모험", "설명", "url.jpg"));

        ServiceReservationCreateRequest serviceReservationCreateRequest = new ServiceReservationCreateRequest("fizz",
                pastDateTime.toLocalDate(), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationCreateRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAST_TIME_RESERVATION.getMessage());
    }

    @Test
    void createDuplicatedReservationExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(futureDateTime.toLocalTime()));
        themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        ServiceReservationCreateRequest request = new ServiceReservationCreateRequest("fizz",
                futureDateTime.toLocalDate(), 1L,
                1L);
        reservationService.create(request);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATED_RESERVATION.getMessage());
    }

    @Test
    void updateNotFoundReservationExceptionTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        ServiceReservationUpdateRequest request = new ServiceReservationUpdateRequest(LocalDate.now().plusDays(1),
                reservationTime.getId());

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_RESERVATION.getMessage());
    }

    @Test
    void updateNotFoundReservationTimeExceptionTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        reservationRepository.create(new Reservation("fizz", futureDateTime.toLocalDate(), reservationTime, theme));

        ServiceReservationUpdateRequest request = new ServiceReservationUpdateRequest(futureDateTime.toLocalDate(), 2L);

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage());
    }

    @Test
    void updatePastReservationExceptionTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        reservationRepository.create(new Reservation("fizz", futureDateTime.toLocalDate(), reservationTime, theme));

        ServiceReservationUpdateRequest request = new ServiceReservationUpdateRequest(LocalDate.now().minusDays(1),
                reservationTime.getId());

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAST_TIME_RESERVATION.getMessage());
    }

    @Test
    void updateDuplicatedReservationExceptionTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        reservationRepository.create(new Reservation("fizz", futureDateTime.toLocalDate(), reservationTime, theme));

        ServiceReservationUpdateRequest request = new ServiceReservationUpdateRequest(futureDateTime.toLocalDate(),
                reservationTime.getId());

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATED_RESERVATION.getMessage());
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
                new ServiceReservationCreateRequest("fizz", futureDateTime.toLocalDate(), 1L, 1L));

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

        reservationService.create(new ServiceReservationCreateRequest("fizz", futureDateTime.toLocalDate(), 1L, 1L));
        reservationService.create(
                new ServiceReservationCreateRequest("fizz2", futureDateTime.toLocalDate().plusDays(1), 1L, 1L));

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
        ReservationTime reservationTime = reservationTimeRepository.create(
                new ReservationTime(futureDateTime.toLocalTime()));
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        reservationRepository.create(new Reservation("fizz", futureDateTime.toLocalDate(), reservationTime, theme));
        reservationService.delete(1L);

        List<ServiceReservationResponse> responseDtos = reservationService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
