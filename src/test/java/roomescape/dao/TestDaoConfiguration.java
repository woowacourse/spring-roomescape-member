package roomescape.dao;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.dao.reservation.FakeReservationDaoImpl;
import roomescape.dao.reservationtime.FakeReservationTimeDaoImpl;
import roomescape.dao.theme.FakeThemeDaoImpl;

@TestConfiguration
public class TestDaoConfiguration {

    @Bean
    public FakeMemberDaoImpl memberDao() {
        return new FakeMemberDaoImpl();
    }

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
