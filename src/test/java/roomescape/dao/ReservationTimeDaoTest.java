package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({ReservationTimeDao.class})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void 이미_예약된_시간이_있으면_true반환() {
        //given
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime newReservationTime = new ReservationTime(LocalTime.parse("10:00"));

        //when
        boolean hasThatTime = reservationTimeDao.existsByStartAt(newReservationTime.getStartAt());

        //then
        assertThat(hasThatTime).isTrue();
    }

    @Test
    void 저장한_시간을_전부_조회한다() {
        // given
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("12:00")));
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));

        // when
        List<ReservationTime> times = reservationTimeDao.findAll();

        // then
        assertThat(times).hasSize(2);
        assertThat(times.get(0).getStartAt()).isEqualTo(LocalTime.parse("10:00"));
        assertThat(times.get(1).getStartAt()).isEqualTo(LocalTime.parse("12:00"));
    }
}
