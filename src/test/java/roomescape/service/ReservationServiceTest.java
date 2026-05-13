package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.service.dto.ServiceReservationRequest;
import roomescape.service.dto.ServiceReservationResponse;
import roomescape.service.dto.ServiceReservationTimeResponse;
import roomescape.service.dto.ServiceThemeResponse;

public class ReservationServiceTest {

    private ReservationService reservationService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void beforeEach() {
        FakeDatabase fakeDatabase = new FakeDatabase();

        reservationRepository = new FakeReservationRepository(fakeDatabase);
        reservationTimeRepository = new FakeReservationTimeRepository(fakeDatabase);
        themeRepository = new FakeThemeRepository(fakeDatabase);

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    void createNotFoundReservationTimeExceptionTest() {
        themeRepository.create(new Theme("피즈의 모험", "설명", "url.jpg"));
        ServiceReservationRequest serviceReservationRequest = new ServiceReservationRequest("fizz",
                LocalDate.of(2026, 5, 2), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationRequest))
                .hasMessage("[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createNotFoundThemeTimeExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        ServiceReservationRequest serviceReservationRequest = new ServiceReservationRequest("fizz",
                LocalDate.of(2026, 5, 2), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(serviceReservationRequest))
                .hasMessage("[ERROR] 해당 ID의 테마를 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    @Test
    void duplicatedReservationExceptionTest() {
        reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        ServiceReservationRequest requestDto = new ServiceReservationRequest("fizz", LocalDate.of(2026, 5, 2), 1L,
                1L);
        reservationService.create(requestDto);

        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage("[ERROR] 동일한 예약이 이미 존재합니다.")
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        ServiceReservationTimeResponse serviceReservationTimeResponse = ServiceReservationTimeResponse.from(
                reservationTime);
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ServiceThemeResponse serviceThemeResponse = ServiceThemeResponse.from(theme);

        ServiceReservationResponse responseDto = reservationService.create(
                new ServiceReservationRequest("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));

        assertThat(responseDto).isEqualTo(
                new ServiceReservationResponse(1L, "fizz", LocalDate.of(2026, 5, 2),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
    }

    @Test
    void readAllTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        ServiceReservationTimeResponse serviceReservationTimeResponse = ServiceReservationTimeResponse.from(
                reservationTime);
        Theme theme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ServiceThemeResponse serviceThemeResponse = ServiceThemeResponse.from(theme);

        reservationService.create(new ServiceReservationRequest("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));
        reservationService.create(new ServiceReservationRequest("fizz2", LocalDate.of(2026, 5, 4), 1L, 1L));

        List<ServiceReservationResponse> responseDtos = reservationService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(
                new ServiceReservationResponse(responseDtos.getFirst().id(), "fizz", LocalDate.of(2026, 5, 2),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
        assertThat(responseDtos.get(1)).isEqualTo(
                new ServiceReservationResponse(responseDtos.get(1).id(), "fizz2", LocalDate.of(2026, 5, 4),
                        serviceReservationTimeResponse,
                        serviceThemeResponse));
    }

    @Test
    void deleteTest() {
        reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        reservationService.create(new ServiceReservationRequest("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));
        reservationService.delete(1L);

        List<ServiceReservationResponse> responseDtos = reservationService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
