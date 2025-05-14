package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ReservationPostRequestByUser;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.dto.response.ReservationTimePostResponse;
import roomescape.entity.MemberRole;
import roomescape.entity.ReservationTime;
import roomescape.service.fake_dao.FakeMemberDao;
import roomescape.service.fake_dao.FakeReservationDao;
import roomescape.service.fake_dao.FakeReservationTimeDao;
import roomescape.service.fake_dao.FakeThemeDao;
import roomescape.web.LoginMember;

class ReservationServiceTest {

    private final ReservationDao reservationDao = new FakeReservationDao();
    private final MemberDao memberDao = new FakeMemberDao();
    private final ReservationTimeDao timeDao = new FakeReservationTimeDao();
    private final ThemeDao themeDao = new FakeThemeDao();
    private final ReservationQueryService reservationQueryService = new ReservationQueryService(reservationDao);
    private final ReservationCommandService reservationCommandService = new ReservationCommandService(reservationDao,
            memberDao,
            timeDao,
            themeDao);

    @Test
    @Disabled
    void createReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0, 0));
        timeDao.create(time);

        ReservationPostResponse reservation = reservationCommandService.createReservationOfLoginMember(
                new ReservationPostRequestByUser(
                        LocalDate.of(2025, 4, 27), 1L, 1L
                ), new LoginMember(1L, "moda", MemberRole.ADMIN));

        assertAll(
                () -> assertThat(reservation.id()).isEqualTo(1),
                () -> assertThat(reservation.date()).isEqualTo(LocalDate.of(2025, 4, 27)),
                () -> assertThat(reservation.time()).isEqualTo(new ReservationTimePostResponse(time))
        );
    }

    @Test
    @Disabled
    void deleteReservation() {
        createReservation();

        reservationCommandService.deleteReservation(1L);

        assertThat(reservationDao.findAll()).hasSize(0);
    }
}
