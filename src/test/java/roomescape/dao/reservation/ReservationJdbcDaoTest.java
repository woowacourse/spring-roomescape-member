package roomescape.dao.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationJdbcDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.ThemeJdbcDao;
import roomescape.dao.TimeDao;
import roomescape.dao.TimeJdbcDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({
        ReservationJdbcDao.class,
        TimeJdbcDao.class,
        ThemeJdbcDao.class
})
@ActiveProfiles("test")
class ReservationJdbcDaoTest extends ReservationDaoContract{

    @Autowired private TimeDao timeDao;
    @Autowired private ThemeDao themeDao;
    @Autowired private ReservationDao reservationDao;
    @Autowired private JdbcTemplate jdbc;

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
