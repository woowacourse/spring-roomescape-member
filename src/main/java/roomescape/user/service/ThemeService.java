package roomescape.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.domain.Theme;
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

        List<ThemeResponse> responses = new ArrayList<>();
        for (Long themeId : themeIds) {
            Theme theme = themeRepository.findById(themeId);

            ThemeResponse response = ThemeResponse.of(theme);
            responses.add(response);
        }
        return responses;
    }
}
