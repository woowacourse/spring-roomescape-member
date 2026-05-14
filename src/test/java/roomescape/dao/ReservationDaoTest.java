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
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        Theme savedTheme = saveTheme("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");

        LocalDate date = LocalDate.of(2026, 5, 5);
        Reservation reservation = new Reservation("브라운", date, savedReservationTime, savedTheme);

        // when
        Reservation savedReservation = reservationDao.save(reservation);

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
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        Theme savedTheme = saveTheme("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");

        LocalDate date = LocalDate.of(2026, 5, 5);

        saveReservation("브라운", date, savedReservationTime, savedTheme);
        saveReservation("로지", date, savedReservationTime, savedTheme);
        saveReservation("러키", date, savedReservationTime, savedTheme);
        saveReservation("러로", date, savedReservationTime, savedTheme);
        saveReservation("밤밤", date, savedReservationTime, savedTheme);

        // when
        List<Reservation> reservations = reservationDao.findAll();

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(5),
                () -> assertThat(reservations.getFirst().getName()).isEqualTo("브라운"),
                () -> assertThat(reservations.getFirst().getDate()).isEqualTo(date),

                () -> assertThat(reservations.getFirst().getTime().getId()).isEqualTo(savedReservationTime.getId()),
                () -> assertThat(reservations.getFirst().getTime().getStartAt()).isEqualTo(savedReservationTime.getStartAt()),

                () -> assertThat(reservations.getFirst().getTheme().getId()).isEqualTo(savedTheme.getId()),
                () -> assertThat(reservations.getFirst().getTheme().getName()).isEqualTo(savedTheme.getName()),
                () -> assertThat(reservations.getFirst().getTheme().getDescription()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(reservations.getFirst().getTheme().getThumbnail()).isEqualTo(savedTheme.getThumbnail())
        );
    }

    @Test
    void 테마와_날짜_및_시간이_일치하는_예약이_존재하는지_확인한다() {
        // given
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        ReservationTime otherReservationTime = saveReservationTime(LocalTime.of(11, 0));

        Theme savedTheme = saveTheme("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        Theme otherTheme = saveTheme("방탈출2", "밤밤과 러로의 방탈출", "https:Fasdg/dfgt");

        LocalDate date = LocalDate.of(2026, 5, 5);
        LocalDate otherDate = LocalDate.of(2026, 5, 6);

        saveReservation("브라운", date, savedReservationTime, savedTheme);

        // when & then
        assertAll(
                () -> assertThat(reservationDao.existsByThemeAndDateAndTime(
                        savedTheme.getId(),
                        date,
                        savedReservationTime.getId()
                )).isTrue(),

                () -> assertThat(reservationDao.existsByThemeAndDateAndTime(
                        otherTheme.getId(),
                        date,
                        savedReservationTime.getId()
                )).isFalse(),

                () -> assertThat(reservationDao.existsByThemeAndDateAndTime(
                        savedTheme.getId(),
                        otherDate,
                        savedReservationTime.getId()
                )).isFalse(),

                () -> assertThat(reservationDao.existsByThemeAndDateAndTime(
                        savedTheme.getId(),
                        date,
                        otherReservationTime.getId()
                )).isFalse()
        );
    }

    @Test
    void 예약을_삭제한다() {
        // given
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        Theme savedTheme = saveTheme("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        LocalDate date = LocalDate.of(2026, 5, 5);

        Reservation savedReservation = saveReservation("예약1", date, savedReservationTime, savedTheme);

        // when
        reservationDao.delete(savedReservation.getId());

        // then
        List<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations).hasSize(0);
    }

    private ReservationTime saveReservationTime(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return timeDao.save(time);
    }

    private Theme saveTheme(String name, String description, String thumbnail) {
        Theme theme = Theme.createWithoutId(name, description, thumbnail);
        return themeDao.save(theme);
    }

    private Reservation saveReservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationDao.save(reservation);
    }
}
