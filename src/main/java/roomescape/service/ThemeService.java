package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int CHECK_STANDARD_OF_DATE = 7;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeRepository.findAllTheme().stream()
            .map(ThemeResponseDto::from)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        themeRepository.saveTheme(theme);
        return ThemeResponseDto.from(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteTheme(id);
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(CHECK_STANDARD_OF_DATE);
        List<Theme> themes = themeRepository.findAllThemeOfRanks(startDate, currentDate);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }
}
