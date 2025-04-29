package roomescape.dao.resetvationTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class})
class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;
    @Autowired
    private JdbcReservationDao jdbcReservationDao;

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
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        jdbcReservationDao.create(new Reservation("test", LocalDate.of(2025, 12, 1), savedReservationTime));

        // when
        boolean result = jdbcReservationTimeDao.deleteIfNoReservation(savedReservationTime.getId());

        // then
        assertThat(result).isFalse();
    }
}
