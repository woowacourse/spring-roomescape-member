package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.theme.ThemeRankResponse;
import roomescape.controller.dto.theme.ThemeRankingQuery;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.CreateThemeCommand;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeResponse createTheme(CreateThemeCommand command) {
        Theme theme = Theme.createNew(
                command.name(),
                command.description(),
                command.imageUrl()
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
        LocalDate toDate = today.minusDays(1L);
        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(fromDate, toDate, query.limit());

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResponse theme = ThemeResponse.from(themes.get(index));
                    return new ThemeRankResponse(rank, theme);
                })
                .toList();
    }
}
