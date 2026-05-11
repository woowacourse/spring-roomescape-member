package roomescape.theme.application.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.application.query.PopularThemeDao;
import roomescape.theme.application.query.PopularThemeResult;
import roomescape.theme.domain.PopularThemePeriod;
import roomescape.theme.domain.Theme;
import roomescape.theme.application.dto.ThemeCreateCommand;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final PopularThemeDao popularThemeDao;

    @Transactional(readOnly = true)
    public ThemeQueryResult findById(Long id) {
        return ThemeQueryResult.from(themeRepository.findById(id)
                .orElseThrow(() -> new ThemeException("존재하지 않는 테마 입니다.")));
    }

    @Transactional(readOnly = true)
    public List<ThemeQueryResult> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeQueryResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PopularThemeResult> findPopularThemes(LocalDate today) {
        return popularThemeDao.findTop10PopularThemes(PopularThemePeriod.from(today))
                .stream()
                .toList();
    }

    public ThemeQueryResult save(ThemeCreateCommand request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeQueryResult.from(themeRepository.save(theme));
    }

    public int delete(long id) {
        return themeRepository.delete(id);
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new ThemeException("이름과 설명이 같은 테마가 이미 존재합니다.");
        }
    }
}
