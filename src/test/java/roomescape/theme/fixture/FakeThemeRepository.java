package roomescape.theme.fixture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {
    private final List<Theme> themes = new ArrayList<>();
    private Long autoIncrement = 0L;

    @Override
    public Theme save(Theme theme) {
        autoIncrement();
        Theme savedTheme = Theme.load(autoIncrement, theme.name(), theme.description(), theme.thumbnailUrl(), theme.isActive());
        themes.add(savedTheme);
        return savedTheme;
    }

    public List<Theme> saveAll(List<Theme> themes) {
        List<Theme> savedThemes = new ArrayList<>();
        for(Theme theme : themes){
            Theme savedTheme = save(theme);
            savedThemes.add(savedTheme);
        }
        return savedThemes;
    }

    private void autoIncrement() {
        autoIncrement = autoIncrement + 1;
    }

    @Override
    public boolean updateStatus(Theme theme) {
        boolean isActive = theme.isActive();
        Optional<Theme> target = findById(theme.id());
        if (target.isEmpty()) {
            return false;
        }
        target.get().updateStatus(isActive);
        return true;
    }

    @Override
    public List<Theme> findAll() {
        return themes.stream().toList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Theme> findByStatus(boolean status) {
        return themes.stream()
                .filter(theme -> theme.isActive() == status)
                .sorted(Comparator.comparing(Theme::name))
                .toList();
    }
}
