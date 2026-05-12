package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeConstraintException;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ThemeResult save(final String name, final String description, final String thumbnailUrl) {
        validateDuplicate(name);

        Theme theme = Theme.createNew(name, description, thumbnailUrl);

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResult.from(savedTheme);
    }

    @Transactional
    public void deleteById(final long themeId) {
        validateReservationExists(themeId);
        themeRepository.deleteById(themeId);
    }

    public List<ThemeResult> getAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResult::from)
                .toList();
    }

    public List<ThemeResult> getPopularThemes(final int period, final int limit) {
        return themeRepository.findPopularThemes(
                        period,
                        limit,
                        LocalDate.now()
                ).stream()
                .map(ThemeResult::from)
                .toList();
    }

    private void validateDuplicate(final String name) {
        if (themeRepository.existsByName(name)) {
            throw new ThemeDuplicateException();
        }
    }

    private void validateReservationExists(final long themeId) {
        if (reservationRepository.existsByThemeId(themeId)) {
            throw new ThemeConstraintException();
        }
    }
}
