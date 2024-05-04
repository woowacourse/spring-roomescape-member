package roomescape.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.domain.ReservationFactory;

@Configuration(proxyBeanMethods = false)
class ServiceConfig {

    @Bean
    public ReservationFactory reservationFactory() {
        return new ReservationFactory();
    }
}
