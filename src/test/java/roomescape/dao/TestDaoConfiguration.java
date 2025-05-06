package roomescape.dao;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDaoConfiguration {

    @Bean
    public FakeReservationTimeDaoImpl reservationTimeDao() {
        return new FakeReservationTimeDaoImpl();
    }

    @Bean
    public FakeReservationDaoImpl reservationDao() {
        return new FakeReservationDaoImpl();
    }

    @Bean
    public FakeThemeDaoImpl themeDao() {
        return new FakeThemeDaoImpl();
    }

}
