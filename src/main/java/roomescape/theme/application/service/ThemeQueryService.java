package roomescape.theme.application.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.application.dto.PopularThemeResult;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.application.dao.PopularThemeDao;
import roomescape.theme.domain.PopularThemePeriod;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ThemeQueryService {

    private final ThemeRepository themeRepository;
    private final PopularThemeDao popularThemeDao;

    public ThemeResult findById(Long id) {
        return ThemeResult.from(themeRepository.findById(id)
                .orElseThrow(() -> new ThemeException("존재하지 않는 테마 입니다.")));
    }

    public List<ThemeResult> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeResult::from)
                .toList();
    }

    public List<PopularThemeResult> findPopularThemes(LocalDate today) {
        return popularThemeDao.findTop10PopularThemes(PopularThemePeriod.from(today))
                .stream()
                .toList();
    }
}
