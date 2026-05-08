package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.user.dto.ThemeResponse;
import roomescape.user.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getTopThemes() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        List<Long> themeIds = themeRepository.findThemeIdTop10(startDate, endDate);

        return themeIds.stream()
            .map(themeRepository::findById)
            .map(ThemeResponse::of)
            .toList();
    }
}
