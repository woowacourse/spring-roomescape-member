package roomescape.reservation.application;

import org.springframework.stereotype.Component;
import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.application.ReferenceChecker;

@Component
public class ThemeReferenceChecker implements ReferenceChecker {

    private final ReservationRepository reservationRepository;

    public ThemeReferenceChecker(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void validateThemeNotReferenced(Long themeId) {
        if (reservationRepository.existsByThemeId(themeId)) {
            throw new BusinessException(ThemeErrorCode.THEME_IN_USE);
        }
    }
}
