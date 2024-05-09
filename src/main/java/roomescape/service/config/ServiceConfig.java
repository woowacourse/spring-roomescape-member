package roomescape.service.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.domain.ReservationFactory;

@Configuration
class ServiceConfig {

    @Bean
    public ReservationFactory reservationFactory(Clock clock) {
        return new ReservationFactory(clock);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
