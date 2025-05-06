package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.infrastructure.dao.ReservationDao;
import roomescape.infrastructure.dao.ReservationTimeDao;
import roomescape.infrastructure.dao.ThemeDao;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.TestFixture.*;

@JdbcTest
class ReservationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    ReservationDao reservationDao;
    ReservationTimeDao reservationTimeDao;
    ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        themeDao = new ThemeDao(jdbcTemplate);
    }

    @Test
    void 예약을_저장할_수_있다() {
        // given
        String name = "두리";
        LocalDate date = TOMORROW;
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        Reservation reservation = new Reservation(name, date, savedReservationTime, savedTheme);

        // when
        Reservation savedReservation = reservationDao.save(reservation);

        // then
        assertAll(
                () -> assertThat(savedReservation.getName()).isEqualTo(name),
                () -> assertThat(savedReservation.getDate()).isEqualTo(date)
        );
    }

    @Test
    void 저장된_전체_예약을_조회할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        int totalCount = reservationDao.findAll().size();

        // when
        Reservation savedReservation = reservationDao.save(new Reservation("예약", TOMORROW, savedReservationTime, savedTheme));

        // when & then
        assertAll(
                () -> assertThat(reservationDao.findAll().size()).isEqualTo(totalCount + 1),
                () -> assertThat(reservationDao.findAll()).contains(savedReservation)
        );
    }

    @Test
    void 특정_예약을_삭제할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        Reservation reservation = new Reservation("예약", LocalDate.now().plusDays(3), savedReservationTime, savedTheme);
        Reservation savedReservation = reservationDao.save(reservation);
        int totalCount = reservationDao.findAll().size();

        // when
        reservationDao.deleteById(savedReservation.getId());

        // then
        assertThat(reservationDao.findAll().size()).isEqualTo(totalCount - 1);
    }

    @Test
    void 특정_시간에_대한_예약이_존재하는지_확인할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        // when
        reservationDao.save(new Reservation("예약", TOMORROW, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.existByTimeId(savedReservationTime.getId())).isTrue();
    }

    @Test
    void 특정_테마에_대한_예약이_존재하는지_확인할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        // when
        reservationDao.save(new Reservation("예약", TOMORROW, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.existByThemeId(savedTheme.getId())).isTrue();
    }

    @Test
    void 중복_예약이_존재하는지_확인할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        Long timeId = savedReservationTime.getId();
        Long themeId = savedTheme.getId();
        LocalDate date = TOMORROW;

        // when
        reservationDao.save(new Reservation("예약", date, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.existByTimeIdAndThemeIdAndDate(timeId, themeId, date)).isTrue();
    }

    @Test
    void 특정_날짜와_테마에_대해_예약이_존재하는_시간대를_확인할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        Long timeId = savedReservationTime.getId();
        Long themeId = savedTheme.getId();
        LocalDate date = TOMORROW;

        // when
        reservationDao.save(new Reservation("예약", date, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.findBookedTimes(themeId, date)).contains(timeId);
    }
}
