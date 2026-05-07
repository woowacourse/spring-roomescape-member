package roomescape.theme.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.controller.dto.CreateThemeRequest;
import roomescape.theme.controller.dto.ThemeRankResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.theme.repository.dto.GetThemeRankingsInRecentDaysParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeResponse addTheme(CreateThemeRequest request) {
        CreateThemeParams params = new CreateThemeParams(
                request.name(),
                request.description(),
                request.imageUrl()
        );
        Theme savedTheme = themeRepository.save(params);

        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void removeRegisteredTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public List<ThemeRankResponse> getThemeRankingsInRecentDays(int days, int limit) {
        List<Theme> themes = themeRepository.findThemesOrderedByReservationCount(
                GetThemeRankingsInRecentDaysParams.of(days, limit)
        );

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResponse themeResponse = ThemeResponse.from(themes.get(index));
                    return new ThemeRankResponse(rank, themeResponse);
                })
                .toList();
    }
}

