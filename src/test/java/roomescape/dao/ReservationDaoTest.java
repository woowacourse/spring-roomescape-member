package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({ReservationDao.class, ReservationTimeDao.class, ThemeDao.class})
class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    @Test
    void 예약을_생성한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime time = ReservationTime.createWithoutId(startAt);
        Theme theme = Theme.createWithoutId("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        ReservationTime savedReservationTime = timeDao.insert(time);
        Theme savedTheme = themeDao.insert(theme);

        LocalDate date = LocalDate.of(2026, 5, 5);
        Reservation reservation = Reservation.createWithoutId("브라운", date, savedReservationTime, savedTheme);

        // when
        Reservation savedReservation = reservationDao.insert(reservation);

        // then
        assertAll(
                () -> assertThat(savedReservation.getId()).isNotNull(),
                () -> assertThat(savedReservation.getName()).isEqualTo(reservation.getName()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTime()).isEqualTo(reservation.getTime()),
                () -> assertThat(savedReservation.getTheme()).isEqualTo(reservation.getTheme())
        );
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime time = ReservationTime.createWithoutId(startAt);
        ReservationTime savedReservationTime = timeDao.insert(time);

        Theme theme = Theme.createWithoutId("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        Theme savedTheme = themeDao.insert(theme);

        LocalDate date = LocalDate.of(2026, 5, 5);

        reservationDao.insert(Reservation.createWithoutId("브라운", date, savedReservationTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("로지", date, savedReservationTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("러키", date, savedReservationTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("러로", date, savedReservationTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("밤밤", date, savedReservationTime, savedTheme));

        // when
        List<Reservation> reservations = reservationDao.select();

        // then
        assertAll(
                () -> assertThat(reservations.size()).isEqualTo(5),
                () -> assertThat(reservations.get(0).getName()).isEqualTo("브라운"),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo(date),

                () -> assertThat(reservations.get(0).getTime().getId()).isEqualTo(savedReservationTime.getId()),
                () -> assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(savedReservationTime.getStartAt()),

                () -> assertThat(reservations.get(0).getTheme().getId()).isEqualTo(savedTheme.getId()),
                () -> assertThat(reservations.get(0).getTheme().getName()).isEqualTo(savedTheme.getName()),
                () -> assertThat(reservations.get(0).getTheme().getDescription()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(reservations.get(0).getTheme().getThumbnail()).isEqualTo(savedTheme.getThumbnail())
        );
    }

    @Test
    void 예약을_삭제한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime time = ReservationTime.createWithoutId(startAt);
        Theme theme = Theme.createWithoutId("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        LocalDate date = LocalDate.of(2026, 5, 5);
        ReservationTime savedReservationTime = timeDao.insert(time);
        Theme savedTheme = themeDao.insert(theme);

        Reservation savedReservation = reservationDao.insert(Reservation.createWithoutId("예약1", date, savedReservationTime, savedTheme));

        // when
        reservationDao.delete(savedReservation.getId());

        // then
        List<Reservation> reservations = reservationDao.select();
        assertThat(reservations.size()).isEqualTo(0);
    }
}
