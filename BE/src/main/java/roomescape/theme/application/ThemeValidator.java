package roomescape.theme.application;

import org.springframework.stereotype.Component;
import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.ReservationRepository;

@Component
public class ThemeValidator {

    private final ReservationRepository reservationRepository;

    public ThemeValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void validateNotReferencedByReservation(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new BusinessException(ThemeErrorCode.THEME_IN_USE);
        }
    }
}
