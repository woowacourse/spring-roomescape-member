package roomescape.reservation.application;

import org.springframework.stereotype.Component;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservationTime.application.ReferenceChecker;

@Component
public class ReservationTimeReferenceChecker implements ReferenceChecker {

    private final ReservationRepository reservationRepository;

    public ReservationTimeReferenceChecker(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void validateReservationTimeNotReferenced(Long timeId) {
        if (reservationRepository.existsByReservationTimeId(timeId)) {
            throw new BusinessException(ReservationTimeErrorCode.RESERVATION_TIME_IN_USE);
        }
    }
}
