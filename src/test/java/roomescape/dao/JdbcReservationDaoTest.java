package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.model.Member;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.infrastructure.dao.JdbcMemberDao;
import roomescape.infrastructure.dao.JdbcReservationDao;
import roomescape.infrastructure.dao.JdbcReservationTimeDao;
import roomescape.infrastructure.dao.JdbcThemeDao;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.TestFixture.*;

@JdbcTest
class JdbcReservationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    JdbcReservationDao reservationDao;
    JdbcReservationTimeDao reservationTimeDao;
    JdbcThemeDao themeDao;
    Long memberId;

    @BeforeEach
    void setUp() {
        reservationDao = new JdbcReservationDao(jdbcTemplate);
        reservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);
        themeDao = new JdbcThemeDao(jdbcTemplate);

        JdbcMemberDao memberDao = new JdbcMemberDao(jdbcTemplate);
        memberId = memberDao.save(new Member("유저", "user@gmail.com", "1234", "user"));
    }

    @Test
    void 예약을_저장할_수_있다() {
        // given
        LocalDate date = TOMORROW;
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        Reservation reservation = new Reservation(memberId, date, savedReservationTime, savedTheme);

        // when
        Reservation savedReservation = reservationDao.save(reservation);

        // then
        assertAll(
                () -> assertThat(savedReservation.getMemberId()).isEqualTo(memberId),
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
        Reservation savedReservation = reservationDao.save(new Reservation(memberId, TOMORROW, savedReservationTime, savedTheme));

        // when & then
        assertAll(
                () -> assertThat(reservationDao.findAll().size()).isEqualTo(totalCount + 1)
        );
    }

    @Test
    void 특정_예약을_삭제할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        Reservation reservation = new Reservation(memberId, LocalDate.now().plusDays(3), savedReservationTime, savedTheme);
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
        reservationDao.save(new Reservation(memberId, TOMORROW, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.existByTimeId(savedReservationTime.getId())).isTrue();
    }

    @Test
    void 특정_테마에_대한_예약이_존재하는지_확인할_수_있다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(DEFAULT_TIME);
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        // when
        reservationDao.save(new Reservation(memberId, TOMORROW, savedReservationTime, savedTheme));

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
        reservationDao.save(new Reservation(memberId, date, savedReservationTime, savedTheme));

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
        reservationDao.save(new Reservation(memberId, date, savedReservationTime, savedTheme));

        // then
        assertThat(reservationDao.findBookedTimes(themeId, date)).contains(timeId);
    }
}
