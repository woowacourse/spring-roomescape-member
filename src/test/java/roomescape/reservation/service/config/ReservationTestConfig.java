package roomescape.reservation.service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.reservation.service.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.time.service.ReservationTimeRepository;
import roomescape.time.service.ReservationTimeService;
import roomescape.time.service.ReservationTimeServiceImpl;

@TestConfiguration
public class ReservationTestConfig {

    @Bean
    public ReservationService reservationService() {
        return new ReservationService(fakeReservationRepository(), fakeReservationTimeService());
    }

    @Bean
    public ReservationRepository fakeReservationRepository() {
        return new FakeReservationRepository();
    }

    @Bean
    public ReservationTimeService fakeReservationTimeService() {
        return new FakeReservationTimeService();
    }

    @Bean
    public ReservationTimeService reservationTimeService() {
        return new ReservationTimeServiceImpl(fakeReservationTimeRepository());
    }

    @Bean
    public ReservationTimeRepository fakeReservationTimeRepository() {
        return new FakeReservationTimeRepository();
    }
}
