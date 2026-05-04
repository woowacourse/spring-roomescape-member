package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.ReservationTheme.ReservationThemeRepository;

@Service
public class ReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationThemeService(ReservationThemeRepository reservationThemeRepository, ReservationRepository reservationRepository) {
        this.reservationThemeRepository = reservationThemeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme addTheme(ThemeCommand themeCommand) {
        return reservationThemeRepository.addTheme(themeCommand);
    }

    public List<Theme> getAllTheme() {
        return reservationThemeRepository.getAllTheme();
    }

    public void deleteTheme(long id) {
        boolean hasTheme = reservationRepository.existsByThemeId(id);

        if(hasTheme) {
            throw new DataReferencedException(ErrorMessage.CANNOT_DELETE_RESERVATION_THEME_IN_USE);
        }

        try {
            reservationThemeRepository.deleteTheme(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataReferencedException(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE);
        }
    }
}
