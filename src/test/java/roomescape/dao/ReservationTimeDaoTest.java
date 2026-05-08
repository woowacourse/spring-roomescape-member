package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({ReservationTimeDao.class})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void 이미_예약된_시간이_있으면_true반환(){
        //given
        reservationTimeDao.save(new ReservationTime(null, LocalTime.parse("10:00")));
        ReservationTime newReservationTime = new ReservationTime(null, LocalTime.parse("10:00"));

        //when
        boolean hasThatTime = reservationTimeDao.existsByStartAt(newReservationTime.getStartAt());

        //then
        assertThat(hasThatTime).isTrue();
    }
}
