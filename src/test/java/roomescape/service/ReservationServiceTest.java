package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import roomescape.config.LoginMember;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.service.fake_dao.FakeMemberDao;
import roomescape.service.fake_dao.FakeReservationDao;
import roomescape.service.fake_dao.FakeReservationTimeDao;
import roomescape.service.fake_dao.FakeThemeDao;

class ReservationServiceTest {

    private final ReservationDao reservationDao = new FakeReservationDao();
    private final MemberDao memberDao = new FakeMemberDao();
    private final ReservationTimeDao timeDao = new FakeReservationTimeDao();
    private final ThemeDao themeDao = new FakeThemeDao();
    private final ReservationQueryService reservationQueryService = new ReservationQueryService(reservationDao,
            memberDao,
            timeDao,
            themeDao);
    private final ReservationCommandService reservationCommandService = new ReservationCommandService(reservationDao,
            memberDao,
            timeDao,
            themeDao);

    @Test
    @Disabled
    void createReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0, 0));
        timeDao.create(time);

        ReservationResponse reservation = reservationCommandService.createReservationOfLoginMember(
                new ReservationRequest(
                        LocalDate.of(2025, 4, 27), 1L, 1L
                ), new LoginMember(1L));

        assertAll(
                () -> assertThat(reservation.id()).isEqualTo(1),
                () -> assertThat(reservation.date()).isEqualTo(LocalDate.of(2025, 4, 27)),
                () -> assertThat(reservation.time()).isEqualTo(new ReservationTimeResponse(time))
        );
    }

    @Test
    @Disabled
    void findAllReservations() {
        createReservation();

        List<ReservationResponse> reservations = reservationQueryService.findAllReservations();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @Disabled
    void deleteReservation() {
        createReservation();

        reservationCommandService.deleteReservation(1L);

        assertThat(reservationDao.findAll()).hasSize(0);
    }
}
