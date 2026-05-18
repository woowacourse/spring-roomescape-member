package roomescape.dao.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationJdbcDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ReservationQueryJdbcDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.ThemeJdbcDao;
import roomescape.dao.TimeDao;
import roomescape.dao.TimeJdbcDao;

@JdbcTest
@Import({TimeJdbcDao.class,
        ThemeJdbcDao.class,
        ReservationJdbcDao.class,
        ReservationQueryJdbcDao.class})
@ActiveProfiles("test")
public class ReservationQueryJdbcDaoTest extends ReservationQueryDaoContract {

    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationQueryDao reservationQueryDao;
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    void clearTime() {
        jdbc.execute("DELETE FROM times");
    }

    @Override
    void clearTheme() {
        jdbc.execute("DELETE FROM themes");
    }

    @Override
    void clearReservation() {
        jdbc.execute("DELETE FROM reservations");
    }

    @Override
    ReservationQueryDao queryDao() {
        return reservationQueryDao;
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
