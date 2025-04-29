package roomescape.dao.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.dao.resetvationTime.JdbcReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class})
class JdbcReservationDaoTest {

    @Autowired
    private JdbcReservationDao jdbcReservationDao;

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @DisplayName("에약을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        Reservation reservation = new Reservation("체체", LocalDate.now(),
                savedReservationTime);

        // when
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // then
        assertThat(savedReservation.getName()).isEqualTo("체체");
    }

    @DisplayName("에약을 데이터베이스에서 조회한다.")
    @Test
    void findAllTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        Reservation reservation1 = new Reservation("체체", LocalDate.now(),
                savedReservationTime);
        Reservation reservation2 = new Reservation("체체", LocalDate.now(),
                savedReservationTime);
        jdbcReservationDao.create(reservation1);
        jdbcReservationDao.create(reservation2);

        // when
        List<Reservation> reservations = jdbcReservationDao.findAll();
        System.out.println(reservations.getFirst().getName());

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        Reservation reservation = new Reservation("체체", LocalDate.now(),
                savedReservationTime);
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        jdbcReservationDao.delete(savedReservation.getId());
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(0);
    }
}
