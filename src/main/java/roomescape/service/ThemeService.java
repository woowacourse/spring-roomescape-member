package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.dto.ThemeCreateData;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ThemeCreateRequest;
import roomescape.service.dto.response.ThemeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final Clock clock;

    private static final int POPULAR_THEME_PERIOD_DAYS = 7;

    public ThemeResponse create(final ThemeCreateRequest request) {
        final Theme themeWithoutId = Theme.create(
                new ThemeCreateData(
                        request.name(),
                        request.description(),
                        request.thumbnailUrl()
                )
        );

        Theme theme = themeRepository.save(themeWithoutId);

        return ThemeResponse.from(theme);
    }

    public void delete(final Long themeId) {
        boolean deleted = themeRepository.deleteById(themeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }

    public List<ThemeResponse> getPopularThemes() {
        final LocalDate today = LocalDate.now(clock);
        final LocalDate startDate = today.minusDays(POPULAR_THEME_PERIOD_DAYS);

        return themeRepository.findPopularThemes(startDate, today)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
