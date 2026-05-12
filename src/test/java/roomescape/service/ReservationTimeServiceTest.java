package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeDatabase;
import roomescape.dao.FakeReservationDao;
import roomescape.dao.FakeReservationTimeDao;
import roomescape.dao.FakeThemeDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.ServiceReservationTimeRequest;
import roomescape.service.dto.ServiceReservationTimeResponse;

public class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    private ReservationDao reservationDao;

    private ReservationTimeDao reservationTimeDao;

    private ThemeDao themeDao;

    @BeforeEach
    void beforeEach() {
        FakeDatabase fakeDatabase = new FakeDatabase();

        reservationDao = new FakeReservationDao(fakeDatabase);
        reservationTimeDao = new FakeReservationTimeDao(fakeDatabase);
        themeDao = new FakeThemeDao(fakeDatabase);

        reservationTimeService = new ReservationTimeService(reservationTimeDao, themeDao, reservationDao);
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
        ReservationTime reservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        reservationTimeDao.create(new ReservationTime(LocalTime.of(11, 0)));

        Theme theme = themeDao.create(new Theme("방탈출1", "방탈출1 설명", "url.jpg"));

        reservationDao.create(new Reservation("fizz", LocalDate.of(2026, 5, 2), reservationTime, theme));

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
