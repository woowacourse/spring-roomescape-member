package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getTopThemes(int limit) {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        List<Long> themeIds = themeRepository.findTopThemeIds(startDate, endDate, limit);
        return themeRepository.findAllByIds(themeIds).stream()
                .map(ThemeResponse::of)
                .collect(Collectors.toList());
    }
}