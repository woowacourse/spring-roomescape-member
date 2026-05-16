package roomescape.reservation.application;

import org.springframework.stereotype.Component;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.ReservationRepository;

@Component
public class DefaultReferenceChecker implements ReservationReferenceChecker {

    private final ReservationRepository reservationRepository;

    public DefaultReferenceChecker(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void validateReservationTimeNotReferenced(Long timeId) {
        if (reservationRepository.existsByReservationTimeId(timeId)) {
            throw new BusinessException(ReservationTimeErrorCode.RESERVATION_TIME_IN_USE);
        }
    }

    @Override
    public void validateThemeNotReferenced(Long themeId) {
        if (reservationRepository.existsByThemeId(themeId)) {
            throw new BusinessException(ThemeErrorCode.THEME_IN_USE);
        }
    }
}
