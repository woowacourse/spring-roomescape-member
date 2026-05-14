package roomescape.dao.reservation;

import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeReservationQueryDao;
import roomescape.fixture.FakeThemeDao;
import roomescape.fixture.FakeTimeDao;

public class ReservationQueryFakeDaoTest extends ReservationQueryDaoContract {

    private final FakeTimeDao timeDao = new FakeTimeDao();
    private final FakeThemeDao themeDao = new FakeThemeDao();
    private final FakeReservationDao reservationDao = new FakeReservationDao();
    private final FakeReservationQueryDao reservationQueryDao = new FakeReservationQueryDao();

    @Override
    void clearTime() {
        FakeDatabase.clearTime();
    }

    @Override
    void clearTheme() {
        FakeDatabase.clearTheme();
    }

    @Override
    void clearReservation() {
        FakeDatabase.clearReservation();
    }

    @Override
    ReservationQueryDao queryDao() {
        return reservationQueryDao;
    }

    @Override
    TimeDao timeDao() {
        return timeDao;
    }

    @Override
    ThemeDao themeDao() {
        return themeDao;
    }

    @Override
    ReservationDao reservationDao() {
        return reservationDao;
    }
}
