package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.theme.CreateThemeCommand;
import roomescape.service.dto.theme.ThemeRankResult;
import roomescape.service.dto.theme.ThemeRankingCondition;
import roomescape.service.dto.theme.ThemeResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public ThemeResult createTheme(CreateThemeCommand command) {
        Theme theme = Theme.createNew(
                command.name(),
                command.description(),
                command.imageUrl()
        );
        Theme savedTheme = themeRepository.save(theme);

        return ThemeResult.from(savedTheme);
    }

    public List<ThemeResult> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResult::from)
                .toList();
    }

    @Transactional
    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public List<ThemeRankResult> getThemeRankings(ThemeRankingCondition condition, LocalDate today) {
        LocalDate fromDate = today.minusDays(condition.days());
        LocalDate toDate = today.minusDays(1L);
        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(fromDate, toDate, condition.limit());

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResult theme = ThemeResult.from(themes.get(index));
                    return new ThemeRankResult(rank, theme);
                })
                .toList();
    }
}
