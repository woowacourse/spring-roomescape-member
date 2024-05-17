package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.service.dto.request.ThemeRequest;
import roomescape.service.dto.response.ThemeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private static final int START_DATE_DIFF = 8;
    private static final int END_DATE_DIFF = 1;
    private static final int TOP_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toDomain();

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findTopThemes() {
        List<Theme> pastReservations = themeRepository.findPastReservations(
                LocalDate.now().minusDays(START_DATE_DIFF),
                LocalDate.now().minusDays(END_DATE_DIFF),
                TOP_LIMIT_COUNT);

        return pastReservations.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
