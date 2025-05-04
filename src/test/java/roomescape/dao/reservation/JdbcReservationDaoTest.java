package roomescape.dao.reservation;

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
import roomescape.dao.reservationTime.JdbcReservationTimeDao;
import roomescape.dao.theme.JdbcThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
class JdbcReservationDaoTest {

    @Autowired
    private JdbcReservationDao jdbcReservationDao;

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @Autowired
    private JdbcThemeDao jdbcThemeDao;

    @DisplayName("에약을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation reservation = Reservation.create("체체", LocalDate.now(),
                savedReservationTime, savedTheme);

        // when
        final Reservation savedReservation = jdbcReservationDao.create(reservation);

        // then
        assertThat(savedReservation.getName()).isEqualTo("체체");
    }

    @DisplayName("에약을 데이터베이스에서 조회한다.")
    @Test
    void findAllTest() {

        // given
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation reservation1 = Reservation.create("체체", LocalDate.now(),
                savedReservationTime, savedTheme);
        final Reservation reservation2 = Reservation.create("체체", LocalDate.now(),
                savedReservationTime, savedTheme);
        jdbcReservationDao.create(reservation1);
        jdbcReservationDao.create(reservation2);

        // when
        final List<Reservation> reservations = jdbcReservationDao.findAll();
        System.out.println(reservations.getFirst().getName());

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // given
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation reservation = Reservation.create("체체", LocalDate.now(),
                savedReservationTime, savedTheme);
        final Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        jdbcReservationDao.delete(savedReservation.getId());
        final List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("특정 테마, 시간, 날짜로 예약을 찾는다.")
    @Test
    void findByThemeAndDateAndTimeTest() {

        //given
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation reservation = Reservation.create("체체", LocalDate.now(),
                savedReservationTime, savedTheme);
        final Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        final Optional<Reservation> optionalReservation = jdbcReservationDao.findByThemeAndDateAndTime(
                savedReservation);

        // then
        assertAll(
                () -> assertThat(optionalReservation.isPresent()).isTrue(),
                () -> assertThat(optionalReservation.get()).isEqualTo(savedReservation)
        );
    }

    @DisplayName("데이터베이스에 존재할 경우 true를 반환한다.")
    @Test
    void existsByIdReturnTrueTest() {

        // given
        final LocalDate date = LocalDate.now().plusDays(1);
        final LocalTime time = LocalTime.of(10, 10);
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation savedReservation = jdbcReservationDao.create(
                Reservation.create("체체", date, savedReservationTime, savedTheme));

        // when & then
        assertThat(jdbcReservationTimeDao.existsById(savedReservation.getId())).isTrue();
    }

    @DisplayName("데이터베이스에 존재하지 않을 경우 false를 반환한다.")
    @Test
    void nonExistsByIdReturnFalseTest() {

        // when & then
        assertThat(jdbcReservationDao.existsById(1L)).isFalse();
    }
}
