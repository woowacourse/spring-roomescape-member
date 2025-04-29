package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.param.CreateThemeParam;
import roomescape.service.result.ThemeResult;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResult> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResult::from)
                .toList();
    }

    public Long create(CreateThemeParam createThemeParam) {
        Theme theme = new Theme(createThemeParam.name(), createThemeParam.description(), createThemeParam.thumbnail());
        return themeRepository.create(theme);
    }

    public ThemeResult findById(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("id에 해당하는 Theme이 없습니다."));

        return ThemeResult.from(theme);
    }

    public void deleteById(final Long themeId) {
        themeRepository.deleteById(themeId);
    }
}
