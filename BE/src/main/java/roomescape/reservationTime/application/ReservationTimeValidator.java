package roomescape.reservationTime.application;

import org.springframework.stereotype.Component;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.ReservationRepository;

@Component
public class ReservationTimeValidator {

    private final ReservationRepository reservationRepository;

    public ReservationTimeValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void validateNotReferencedByReservation(Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new BusinessException(ReservationTimeErrorCode.RESERVATION_TIME_IN_USE);
        }
    }
}
