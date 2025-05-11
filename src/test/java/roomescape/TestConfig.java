package roomescape;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.service.TestTime;
import roomescape.domain.currentdate.CurrentDateTime;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public CurrentDateTime currentDate() {
        return new TestTime();
    }

    @Bean
    @Primary
    public ReservationDao reservationDao() {
        return new FakeReservationDaoImpl();
    }

    @Bean
    @Primary
    public ReservationTimeDao reservationTimeDao() {
        return new FakeReservationTimeDaoImpl();
    }

}
