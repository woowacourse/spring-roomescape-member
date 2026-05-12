package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeConstraintException;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.theme.exception.ThemeErrorCode;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Theme save(final String name, final String description, final String thumbnailUrl) {
        Theme nonIdTheme = Theme.createNew(name, description, thumbnailUrl);

        if (themeRepository.existsByName(name)) {
            throw new ThemeDuplicateException();
        }

        return themeRepository.save(nonIdTheme);
    }

    @Transactional
    public void deleteById(final long themeId) {
        if(reservationRepository.existsByThemeId(themeId))
            throw new ThemeConstraintException();
        themeRepository.deleteById(themeId);
    }

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public List<Theme> getPopularThemes(final int period, final int limit) {
        return themeRepository.findPopularThemes(
                period,
                limit,
                LocalDate.now()
        );
    }

}
