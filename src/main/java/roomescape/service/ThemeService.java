package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeRankResponse;
import roomescape.controller.dto.ThemeRankingQuery;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeResponse createTheme(ThemeRequest request) {
        Theme theme = Theme.createNew(
                request.name(),
                request.description(),
                request.imageUrl()
        );
        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public List<ThemeRankResponse> getThemeRankings(ThemeRankingQuery query, LocalDate today) {
        LocalDate fromDate = today.minusDays(query.days());
        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(fromDate, today, query.limit());

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResponse theme = ThemeResponse.from(themes.get(index));
                    return new ThemeRankResponse(rank, theme);
                })
                .toList();
    }
}
