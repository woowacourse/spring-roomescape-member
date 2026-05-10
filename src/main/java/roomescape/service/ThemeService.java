package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeRankResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.global.exception.InvalidRankingConditionException;

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

    public List<ThemeRankResponse> getThemeRankings(int days, int limit, LocalDate today) {
        validateRankingCondition(days, limit);

        LocalDate fromDate = today.minusDays(days);
        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(fromDate, today, limit);

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResponse theme = ThemeResponse.from(themes.get(index));
                    return new ThemeRankResponse(rank, theme);
                })
                .toList();
    }

    private void validateRankingCondition(int days, int limit) {
        if (days < 1) {
            throw new InvalidRankingConditionException("조회 기간은 1일 이상이어야 합니다.");
        }
        if (limit < 1) {
            throw new InvalidRankingConditionException("조회 개수는 1개 이상이어야 합니다.");
        }
    }
}
