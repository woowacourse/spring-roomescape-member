package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.theme.Theme;
import roomescape.theme.dao.ThemeDao;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationDaoTest {
    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 예약_시간을_추가할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                theme
        );

        // when
        reservationDao.create(reservation);
        List<Reservation> reservationDaoAll = reservationDao.findAll();

        // then
        assertThat(reservationDaoAll.size()).isEqualTo(1);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Long themeId = themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                new Theme(themeId, theme.getName(), theme.getDescription(), theme.getThumbnail())
        );
        reservationDao.create(reservation);

        // when
        List<Reservation> reservationDaoAll = reservationDao.findAll();

        // then
        assertThat(reservationDaoAll.getFirst().getName()).isEqualTo("포라");
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Long themeId = themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                new Theme(themeId, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        reservationDao.create(reservation);
        int beforeSize = reservationDao.findAll().size();

        // when
        reservationDao.delete(id);
        int afterSize = reservationDao.findAll().size();

        // then
        assertThat(beforeSize).isEqualTo(1);
        assertThat(afterSize).isEqualTo(0);
    }

    @Test
    void timeId로_예약을_조회한다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Long themeId = themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                new Theme(themeId, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        reservationDao.create(reservation);
        // when
        Optional<Reservation> foundReservation = reservationDao.findByTimeId(id);
        // then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getName()).isEqualTo("포라");
    }

    @Test
    void id로_예약을_조회한다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        Long id = reservationTimeDao.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Long themeId = themeDao.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                new ReservationTime(id, reservationTime.getStartAt()),
                new Theme(themeId, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        reservationDao.create(reservation);
        // when
        Optional<Reservation> foundReservation = reservationDao.findById(1L);
        // then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getName()).isEqualTo("포라");
    }
}
