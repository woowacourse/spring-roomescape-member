package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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

        return themeRepository.findThemeIdTop10(startDate, endDate).stream()
                .map(themeRepository::findById)
                .map(ThemeResponse::of)
                .collect(Collectors.toList());
    }
}
