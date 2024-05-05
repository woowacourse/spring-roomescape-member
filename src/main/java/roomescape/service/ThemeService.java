package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ThemeDto;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int COUNT_OF_DAY = 7;
    private static final int COUNT_OF_RANKING = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAllThemes();
    }

    public Theme saveTheme(ThemeDto themeDto) {
        Theme theme = Theme.from(themeDto);
        return themeRepository.saveTheme(theme)
                .orElseThrow(() -> new BadRequestException("[ERROR] 데이터가 저장되지 않습니다."));
    }

    public void deleteTheme(long id) {
        themeRepository.deleteThemeById(id);
    }

    public List<Theme> findPopularThemes() {
        LocalDate startDate = LocalDate.now().minusDays(1 + COUNT_OF_DAY);
        LocalDate endDate = LocalDate.now().minusDays(1);
        return themeRepository.findThemeRankingByDate(startDate, endDate, COUNT_OF_RANKING);
    }
}
