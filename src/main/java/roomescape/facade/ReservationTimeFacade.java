package roomescape.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.utils.DateTimeConverter;

@Component
public class ReservationTimeFacade {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeFacade(ReservationService reservationService, ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationService.hasReservationsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용 중인 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeService.deleteTime(id);
    }

    @Transactional
    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeService.findById(request.timeId());

        return reservationService.addReservation(new Reservation(
                        request.name(),
                        DateTimeConverter.dateConverter(request.date()),
                        reservationTime
                )
        );
    }
}
