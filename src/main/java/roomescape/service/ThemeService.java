package roomescape.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;
import roomescape.repository.ReservedThemeChecker;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservedThemeChecker reservedThemeChecker;

    public ThemeService(ThemeRepository themeRepository, ReservedThemeChecker reservedThemeChecker) {
        this.themeRepository = themeRepository;
        this.reservedThemeChecker = reservedThemeChecker;
    }

    public Theme addTheme(ThemeRequestDto themeRequestDto) {
        Theme theme = new Theme(null, themeRequestDto.name(), themeRequestDto.description(),
                themeRequestDto.thumbnail());
        return themeRepository.addTheme(theme);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.getAllTheme();
    }

    public void deleteTheme(Long id) {
        if (reservedThemeChecker.isReservedTheme(id)) {
            throw new IllegalArgumentException("Theme is already reserved.");
        }
        themeRepository.deleteTheme(id);
    }

    public Theme getThemeById(Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> getWeeklyBestThemes() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<Long> bestThemesIds = reservedThemeChecker.getBestThemesIdInDays(now.minusDays(8), now.minusDays(1));
        return themeRepository.findAllByIdIn(bestThemesIds);
    }
}
