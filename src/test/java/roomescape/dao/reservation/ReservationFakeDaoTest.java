package roomescape.dao.reservation;

import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeThemeDao;
import roomescape.fixture.FakeTimeDao;

public class ReservationFakeDaoTest extends ReservationDaoContract{

    private final FakeTimeDao timeDao = new FakeTimeDao();
    private final FakeThemeDao themeDao = new FakeThemeDao();
    private final FakeReservationDao reservationDao = new FakeReservationDao();

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
    ReservationDao reservationDao() {
        return reservationDao;
    }

    @Override
    TimeDao timeDao() {
        return timeDao;
    }

    @Override
    ThemeDao themeDao() {
        return themeDao;
    }
}
