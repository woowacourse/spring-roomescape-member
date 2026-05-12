package roomescape.domain.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.PopularThemeResult;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.repository.ThemeReservationTimeResult;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.PopularThemeResponse;
import roomescape.domain.theme.response.PopularThemesResponse;
import roomescape.domain.theme.response.ThemeReservationTimeResponse;
import roomescape.domain.theme.response.ThemeReservationTimesResponse;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.response.ThemesResponse;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ThemesResponse findAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(responses);
    }

    public ThemeReservationTimesResponse findAllThemeReservationTimes(Long themeId, LocalDate date) {
        List<ThemeReservationTimeResult> timeResults = themeRepository.findAllReservationTimesByThemeIdAndDate(
                themeId,
                date
        );

        List<ThemeReservationTimeResponse> times = timeResults.stream()
                .map(ThemeReservationTimeResponse::from)
                .toList();

        return new ThemeReservationTimesResponse(times);
    }

    public PopularThemesResponse findPopularThemes(Integer period, Integer limit) {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = today.minusDays(period);
        LocalDate endDate = today.minusDays(1);

        List<PopularThemeResult> results = themeRepository.findPopularThemes(
                startDate,
                endDate,
                limit
        );

        List<PopularThemeResponse> popularThemes = results.stream()
                .map(PopularThemeResponse::from)
                .toList();

        return new PopularThemesResponse(popularThemes);
    }

    @Transactional
    public ThemeResponse saveTheme(ThemeCreateRequest request) {
        Theme theme = Theme.create(
                request.name(),
                request.description(),
                request.thumbnailUrl()
        );

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    @Transactional
    public void deleteThemeById(Long themeId) {
        try {
            themeRepository.deleteById(themeId);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException();
        }
    }
}
