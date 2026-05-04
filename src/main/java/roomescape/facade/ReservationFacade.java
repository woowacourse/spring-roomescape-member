package roomescape.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.service.ReservationService;
import roomescape.service.TimeService;

@Component
public class ReservationFacade {

    private final ReservationService reservationService;
    private final TimeService timeService;

    public ReservationFacade(ReservationService reservationService, TimeService timeService) {
        this.reservationService = reservationService;
        this.timeService = timeService;
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationService.hasReservationsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용 중인 예약이 존재하여 삭제할 수 없습니다.");
        }
        timeService.deleteTime(id);
    }
}
