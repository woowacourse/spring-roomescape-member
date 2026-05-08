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
        Reservation reservation = Reservation.createWithoutId("브라운", date, savedReservationTime, savedTheme);

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
    void 테마_아이디와_선택_날짜에_해당하는_예약_목록을_조회한다() {
        // given
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        Theme savedTheme = saveTheme("방탈출1", "로지와 러키의 방탈출", "https:fsof/ommff");
        Theme savedTheme2 = saveTheme("방탈출2", "밤밤과 러로의 방탈출", "https:fsof/sdafjifdsmmff");

        LocalDate date = LocalDate.of(2026, 5, 5);
        saveReservation("러키", date, savedReservationTime, savedTheme);
        saveReservation("로지", date, savedReservationTime, savedTheme2);

        // when
        List<Reservation> reservationsOnCondition = reservationDao.findByThemeIdAndDate(savedTheme.getId(), date);

        // then
        assertAll(
                () -> assertThat(reservationsOnCondition).hasSize(1),
                () -> assertThat(reservationsOnCondition.getFirst().getName()).isEqualTo("러키"),
                () -> assertThat(reservationsOnCondition.getFirst().getDate()).isEqualTo(date),

                () -> assertThat(reservationsOnCondition.getFirst().getTime().getId()).isEqualTo(savedReservationTime.getId()),
                () -> assertThat(reservationsOnCondition.getFirst().getTime().getStartAt()).isEqualTo(savedReservationTime.getStartAt()),

                () -> assertThat(reservationsOnCondition.getFirst().getTheme().getId()).isEqualTo(savedTheme.getId()),
                () -> assertThat(reservationsOnCondition.getFirst().getTheme().getName()).isEqualTo(savedTheme.getName()),
                () -> assertThat(reservationsOnCondition.getFirst().getTheme().getDescription()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(reservationsOnCondition.getFirst().getTheme().getThumbnail()).isEqualTo(savedTheme.getThumbnail())
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
        ReservationTime time = ReservationTime.createWithoutId(startAt);
        return timeDao.save(time);
    }

    private Theme saveTheme(String name, String description, String thumbnail) {
        Theme theme = Theme.createWithoutId(name, description, thumbnail);
        return themeDao.save(theme);
    }

    private Reservation saveReservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        Reservation reservation = Reservation.createWithoutId(name, date, time, theme);
        return reservationDao.save(reservation);
    }
}
