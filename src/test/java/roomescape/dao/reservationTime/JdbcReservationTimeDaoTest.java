package roomescape.dao.reservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.dao.theme.JdbcThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;
    @Autowired
    private JdbcReservationDao jdbcReservationDao;
    @Autowired
    private JdbcThemeDao jdbcThemeDao;

    @DisplayName("예약 시간을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));

        // when
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // then
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 10));
    }

    @DisplayName("데이터이스에 있는 예약 시간 정보들을 가져온다.")
    @Test
    void findAllTest() {

        // given
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.of(10, 10));
        jdbcReservationTimeDao.create(reservationTime1);
        ReservationTime reservationTime2 = new ReservationTime(LocalTime.of(11, 10));
        jdbcReservationTimeDao.create(reservationTime2);

        // when
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAll();

        // then
        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @DisplayName("데이터베이스에서 예약 시간이 삭제될 경우 true를 반환한다.")
    @Test
    void deleteIfNoExistReservationReturnTrueTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // when
        boolean result = jdbcReservationTimeDao.deleteIfNoReservation(savedReservationTime.getId());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("데이터베이스에서 예약이 존재하여 예약 시간이 삭제되지 않을 경우 false를 반환한다.")
    @Test
    void deleteIfExistReservationReturnFalseTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        Theme theme = new Theme("test", "test", "test");
        Theme savedTheme = jdbcThemeDao.create(theme);
        jdbcReservationDao.create(
                Reservation.create("test", LocalDate.now(), savedReservationTime, savedTheme));

        // when
        boolean result = jdbcReservationTimeDao.deleteIfNoReservation(savedReservationTime.getId());

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("데이터베이스에서 id, 테마, 날짜로 시간을 찾는다.")
    @Test
    void findByIdAndDateAndThemeTest() {

        // given
        LocalTime time = LocalTime.of(10, 10);
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = new Theme("test", "test", "test");
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));
        Theme savedTheme = jdbcThemeDao.create(theme);
        Reservation reservation = Reservation.create("test", date, savedReservationTime, savedTheme);
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        Optional<ReservationTime> optionalReservationTime = jdbcReservationTimeDao.findByIdAndDateAndTheme(
                savedReservationTime.getId(), savedTheme.getId(), date);

        // then
        assertAll(
                () -> assertThat(optionalReservationTime.isPresent()).isTrue(),
                () -> assertThat(optionalReservationTime.get()).isEqualTo(savedReservationTime)
        );
    }
}
