package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final static int RANKS_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme theme = themeRepository.create(themeRequest);
        return ThemeResponse.from(theme);
    }

    public void removeTheme(Long id) {
        themeRepository.delete(id);
    }

    public List<ThemeResponse> getThemeAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public List<ThemeResponse> getThemeRanks() {
        LocalDate currentDay = LocalDate.now().minusDays(1);
        LocalDate lastWeekDay = currentDay.minusWeeks(1);
        List<Theme> themes = themeRepository.findByRanks(currentDay.toString(), lastWeekDay.toString(),
                RANKS_LIMIT_COUNT);
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public ThemeResponse findById(Long id) {
        Theme theme = themeRepository.selectById(id);
        return ThemeResponse.from(theme);
    }
}
