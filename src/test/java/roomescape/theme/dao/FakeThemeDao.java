package roomescape.theme.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.Theme;

public class FakeThemeDao implements ThemeDao {

    private final List<Theme> fakeThemes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeThemeDao(Theme... themes) {
        Arrays.stream(themes)
                .forEach(theme -> fakeThemes.add(theme));
    }

    @Override
    public Long create(Theme theme) {
        Theme themeWithId = new Theme(index.getAndIncrement(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
        fakeThemes.add(themeWithId);
        return themeWithId.getId();
    }

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(fakeThemes);
    }

    @Override
    public void delete(long id) {
        fakeThemes.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Optional<Theme> findByName(String name) {
        return fakeThemes.stream()
                .filter(theme -> theme.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return fakeThemes.stream().filter(theme -> theme.getId().equals(id)).findFirst();
    }
}
