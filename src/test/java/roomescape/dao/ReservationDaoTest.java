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
        ReservationTime savedTime = saveTime(10, 0);
        Theme savedTheme = saveTheme("방탈출1", "설명", "https://asdfsdf.sdfs");
        Reservation reservation = Reservation.createWithoutId("브라운", LocalDate.of(2026, 5, 5), savedTime, savedTheme);

        // when
        Reservation saved = reservationDao.insert(reservation);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(reservation.getName()),
                () -> assertThat(saved.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(saved.getTime()).isEqualTo(reservation.getTime()),
                () -> assertThat(saved.getTheme()).isEqualTo(reservation.getTheme())
        );
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        ReservationTime savedTime = saveTime(10, 0);
        Theme savedTheme = saveTheme("방탈출1", "설명", "https://asdfsdf.sdfs");
        LocalDate date = LocalDate.of(2026, 5, 5);

        reservationDao.insert(Reservation.createWithoutId("브라운", date, savedTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("로지", date, savedTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("러키", date, savedTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("러로", date, savedTime, savedTheme));
        reservationDao.insert(Reservation.createWithoutId("밤밤", date, savedTime, savedTheme));

        // when
        List<Reservation> reservations = reservationDao.select();

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(5),
                () -> assertThat(reservations.getFirst().getName()).isEqualTo("브라운")
        );
    }

    @Test
    void 테마_아이디와_선택_날짜에_해당하는_예약_목록을_조회한다() {
        // given
        ReservationTime savedTime = saveTime(10, 0);
        Theme theme1 = saveTheme("방탈출1", "설명1", "https://asdfsdf.sdfs");
        Theme theme2 = saveTheme("방탈출2", "설명2", "https://asdfsdf.sdfs");
        LocalDate date = LocalDate.of(2026, 5, 5);

        reservationDao.insert(Reservation.createWithoutId("러키", date, savedTime, theme1));
        reservationDao.insert(Reservation.createWithoutId("로지", date, savedTime, theme2));

        // when
        List<Reservation> result = reservationDao.selectByThemeIdAndDate(theme1.getId(), date);

        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.getFirst().getName()).isEqualTo("러키")
        );
    }

    @Test
    void 예약을_삭제한다() {
        // given
        ReservationTime savedTime = saveTime(10, 0);
        Theme savedTheme = saveTheme("방탈출1", "설명", "https://asdfsdf.sdfs");
        Reservation saved = reservationDao.insert(
                Reservation.createWithoutId("예약1", LocalDate.of(2026, 5, 5), savedTime, savedTheme));

        // when
        reservationDao.delete(saved.getId());

        // then
        assertThat(reservationDao.select()).isEmpty();
    }

    private ReservationTime saveTime(int hour, int minute) {
        return timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(hour, minute)));
    }

    private Theme saveTheme(String name, String description, String thumbnail) {
        return themeDao.insert(Theme.createWithoutId(name, description, thumbnail));
    }
}
