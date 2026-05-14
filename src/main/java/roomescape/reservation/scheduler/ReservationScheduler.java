package roomescape.reservation.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import roomescape.reservation.service.ReservationService;

@Component
public class ReservationScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReservationScheduler.class);

    private final ReservationService reservationService;

    public ReservationScheduler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void completeExpiredReservations() {
        int updated = reservationService.completeExpiredReservations();
        log.info("[Scheduler] COMPLETED 처리된 예약 수: {}", updated);
    }
}
