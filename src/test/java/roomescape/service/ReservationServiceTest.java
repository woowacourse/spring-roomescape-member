package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.fake_dao.FakeReservationDao;
import roomescape.service.fake_dao.FakeReservationTimeDao;
import roomescape.service.fake_dao.FakeThemeDao;

class ReservationServiceTest {

    private final ReservationDao reservationDao = new FakeReservationDao();
    private final ReservationTimeDao timeDao = new FakeReservationTimeDao();
    private final ThemeDao themeDao = new FakeThemeDao();
    private final ReservationService reservationService = new ReservationService(reservationDao, timeDao, themeDao);

    @Test
    @Disabled
    void createReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0, 0));
        timeDao.create(time);

        ReservationResponse reservation = reservationService.createReservation(new ReservationRequest(
                "moda", LocalDate.of(2025, 4, 27), 1L, 1L
        ));

        assertAll(
                () -> assertThat(reservation.id()).isEqualTo(1),
                () -> assertThat(reservation.name()).isEqualTo("moda"),
                () -> assertThat(reservation.date()).isEqualTo(LocalDate.of(2025, 4, 27)),
                () -> assertThat(reservation.time()).isEqualTo(ReservationTimeResponse.from(time))
        );
    }

    @Test
    @Disabled
    void findAllReservations() {
        createReservation();

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @Disabled
    void deleteReservation() {
        createReservation();

        reservationService.deleteReservation(1L);

        assertThat(reservationDao.findAll()).hasSize(0);
    }
}
