package roomescape.dao.resetvationTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(JdbcReservationTimeDao.class)
class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

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

    @DisplayName("데이터이스에 있는 예약 시간 정보를 삭제한다.")
    @Test
    void deleteTest() {

        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // when
        jdbcReservationTimeDao.delete(savedReservationTime.getId());
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAll();

        // then
        assertThat(reservationTimes.size()).isEqualTo(0);
    }
}
