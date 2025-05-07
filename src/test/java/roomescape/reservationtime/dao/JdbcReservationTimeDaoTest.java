package roomescape.reservationtime.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservationtime.ReservationTime;

import org.junit.jupiter.api.Test;

@Import(JdbcReservationTimeDao.class)
@JdbcTest
@Sql({"/sql/test-schema.sql", "/sql/test-data.sql"})
class JdbcReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void 예약_시간을_추가할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));

        // when
        reservationTimeDao.create(reservationTime);
        List<ReservationTime> reservationTimeDaoAll = reservationTimeDao.findAll();

        // then
        assertThat(reservationTimeDaoAll.size()).isEqualTo(5);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // when
        List<ReservationTime> reservationTimeDaoAll = reservationTimeDao.findAll();

        // then
        assertThat(reservationTimeDaoAll.size()).isEqualTo(4);
        assertThat(reservationTimeDaoAll.getFirst().getStartAt()).isEqualTo(LocalTime.of(9, 0));
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // when
        reservationTimeDao.delete(4L);
        int afterSize = reservationTimeDao.findAll().size();

        // then
        assertThat(afterSize).isEqualTo(3);
    }
}
