package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme addTheme(AddThemeRequest addThemeRequest) {
        return themeRepository.addTheme(addThemeRequest.toEntity());
    }

    public List<Theme> getAllTheme() {
        return themeRepository.getAllTheme();
    }

    @Transactional
    public void deleteTheme(long id) {
        boolean hasTheme = reservationRepository.existsByThemeId(id);

        if(hasTheme) {
            throw new DataReferencedException(ErrorMessage.CANNOT_DELETE_THEME_IN_USE);
        }

        try {
            themeRepository.deleteTheme(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataReferencedException(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE);
        }
    }

    public List<ThemeWithCount> getPopularTheme(PopularConditionRequest popularConditionRequest) {
        return themeRepository.getPopularTheme(popularConditionRequest);
    }
}
