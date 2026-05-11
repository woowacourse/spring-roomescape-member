package roomescape.fixture.fake;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.theme.application.query.PopularThemeResult;
import roomescape.theme.application.query.PopularThemeDao;
import roomescape.theme.domain.PopularThemePeriod;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository, PopularThemeDao {

    private final Map<Long, Theme> themes = new LinkedHashMap<>();
    private final List<PopularThemeResult> popularThemes = new ArrayList<>();
    private Long idHolder = 1L;

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public List<Theme> findAll() {
        return themes.values().stream()
                .toList();
    }

    @Override
    public List<PopularThemeResult> findTop10PopularThemes(PopularThemePeriod period) {
        return popularThemes;
    }

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = theme.withId(idHolder);
        themes.put(idHolder++, savedTheme);
        return savedTheme;
    }

    @Override
    public Integer delete(long id) {
        int beforeSize = themes.size();
        themes.remove(id);
        int afterSize = themes.size();

        if (beforeSize != afterSize) {
            return 1;
        }

        return 0;
    }

    @Override
    public Boolean existsByNameAndDescription(Theme theme) {
        return themes.values().stream()
                .anyMatch(savedTheme -> savedTheme.equals(theme));
    }

    public void savePopularTheme(PopularThemeResult popularTheme) {
        popularThemes.add(popularTheme);
    }
}
