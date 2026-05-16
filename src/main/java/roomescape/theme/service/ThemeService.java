package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.code.RoomEscapeErrorCode;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.ThemeTimeQueryRepository;
import roomescape.theme.service.dto.ThemeTimeAvailability;
import roomescape.theme.service.exception.ThemeNotFoundException;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ThemeTimeQueryRepository themeTimeQueryRepository;

    public List<Theme> getPopularThemes(int limit) {
        final LocalDate today = LocalDate.now();
        final LocalDate endDate = today.minusDays(1);
        final LocalDate startDate = today.minusDays(8);

        return themeRepository.findPopularThemes(limit, startDate, endDate);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public List<ThemeTimeAvailability> getThemeTimeAvailability(long themId, LocalDate date) {
        return themeTimeQueryRepository.findThemeAvailableTime(themId, date);
    }

    public Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(RoomEscapeErrorCode.THEME_NOT_FOUND));
    }

    public Theme saveTheme(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.of(name, description, thumbnailUrl));
    }

    public void deleteTheme(long id) {
        themeRepository.delete(id);
    }
}
