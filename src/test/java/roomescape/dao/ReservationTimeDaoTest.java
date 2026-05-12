package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({ReservationDao.class, ReservationTimeDao.class, ThemeDao.class})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao timeDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ThemeDao themeDao;

    @Test
    void 예약_시간을_생성한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);

        // when
        ReservationTime savedReservationTime = timeDao.save(reservationTime);

        // then
        assertAll(
                () -> assertThat(savedReservationTime.getId()).isNotNull(),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt)
        );
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        // given
        saveReservationTime(LocalTime.of(10, 0));
        saveReservationTime(LocalTime.of(11, 0));
        saveReservationTime(LocalTime.of(12, 0));
        saveReservationTime(LocalTime.of(13, 0));
        saveReservationTime(LocalTime.of(14, 0));

        // when
        List<ReservationTime> reservationTimes = timeDao.findAll();

        // then
        assertAll(
                () -> assertThat(reservationTimes).hasSize(5),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo(LocalTime.of(10, 0))
        );
    }

    @Test
    void 아이디에_맞는_예약_시간을_조회한다() {
        // given
        ReservationTime reservationTime = saveReservationTime(LocalTime.of(10, 0));

        // when
        Optional<ReservationTime> selectReservationTime = timeDao.findById(reservationTime.getId());

        // then
        assertAll(
                () -> assertThat(selectReservationTime).isPresent(),
                () -> assertThat(selectReservationTime.get().getStartAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @Test
    void 테마_아이디와_선택_날짜에_해당하는_예약_시간을_조회한다() {
        // given
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));
        ReservationTime otherReservationTime = saveReservationTime(LocalTime.of(11, 0));
        ReservationTime otherDateReservationTime = saveReservationTime(LocalTime.of(12, 0));

        Theme savedTheme = saveTheme("방탈출", "로지와 러키의 방탈출", "https:fsof/ommff");
        Theme otherTheme = saveTheme("공포방", "밤밤과 러로의 방탈출", "https:fsof/sdafjifdsmmff");

        LocalDate date = LocalDate.of(2026, 5, 5);
        saveReservation("러키", date, savedReservationTime, savedTheme);
        saveReservation("로지", date, otherReservationTime, otherTheme);
        saveReservation("러로", date.plusDays(1), otherDateReservationTime, savedTheme);

        // when
        List<ReservationTime> reservationTimesOnCondition = timeDao.findByThemeIdAndDate(savedTheme.getId(), date);

        // then
        assertAll(
                () -> assertThat(reservationTimesOnCondition).hasSize(1),
                () -> assertThat(reservationTimesOnCondition)
                        .extracting(ReservationTime::getId)
                        .containsExactly(savedReservationTime.getId()),
                () -> assertThat(reservationTimesOnCondition)
                        .extracting(ReservationTime::getId)
                        .doesNotContain(otherReservationTime.getId(), otherDateReservationTime.getId())
        );
    }

    @Test
    void 예약_시간을_삭제한다() {
        // given
        ReservationTime savedReservationTime = saveReservationTime(LocalTime.of(10, 0));

        // when
        timeDao.delete(savedReservationTime.getId());

        // then
        List<ReservationTime> reservationTimes = timeDao.findAll();
        assertThat(reservationTimes).hasSize(0);
    }

    private ReservationTime saveReservationTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);
        return timeDao.save(reservationTime);
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
