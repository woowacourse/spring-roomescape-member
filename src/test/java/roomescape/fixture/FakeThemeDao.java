package roomescape.fixture;

import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.ThemeRow;

import java.util.List;
import java.util.Optional;

import static roomescape.fixture.FakeDatabase.generateThemeId;
import static roomescape.fixture.FakeDatabase.themes;

public class FakeThemeDao implements ThemeDao {
    @Override
    public List<ThemeRow> findAll() {
        return themes.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<ThemeRow> findById(Long id) {
        ThemeRow theme = themes.get(id);

        if (theme == null) {
            return Optional.empty();
        }
        return Optional.of(theme);
    }

    @Override
    public ThemeRow create(ThemeRow theme) {
        boolean duplicate = existsByName(theme.name());

        if (duplicate) {
            throw new DuplicateKeyException("uk_theme_name");
        }

        Long id = generateThemeId();
        ThemeRow newTheme = new ThemeRow(id, theme.name(), theme.thumbnailUrl(), theme.description());
        themes.put(id, newTheme);
        return newTheme;
    }

    @Override
    public int delete(Long id) {
        ThemeRow remove = themes.remove(id);
        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsByName(String name) {
        return themes.values()
                .stream()
                .anyMatch(theme -> theme.name().equals(name));
    }

    @Override
    public boolean existsById(Long id) {
        return themes.get(id) != null;
    }
}
