package roomescape.controller.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.collection.MemoryReservationRepository;
import roomescape.repository.collection.MemoryReservationTimeRepository;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

@Configuration
@Profile("console")
public class ConsoleConfig {

    @Bean
    public ReservationRepository reservationRepository() {
        return new MemoryReservationRepository();
    }

    @Bean
    public ReservationTimeRepository reservationTimeRepository() {
        return new MemoryReservationTimeRepository();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ReservationService reservationService,
                                               ReservationTimeService reservationTimeService) {
        return new ReservationConsoleController(reservationService, reservationTimeService);
    }
}
