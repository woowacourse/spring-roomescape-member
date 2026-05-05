package roomescape.theme.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public class FakeThemeRepository implements ThemeRepository{
    private final List<Theme> themes = new ArrayList<>();
    private Long autoIncrement = 0L;

    @Override
    public Theme save(Theme theme) {
        autoIncrement();
        Theme savedTheme = Theme.load(autoIncrement, theme.name(), theme.description(), theme.thumbnailUrl(), theme.isActive());
        themes.add(savedTheme);
        return savedTheme;
    }

    @Override
    public boolean updateStatus(Theme theme) {
        boolean isActive = theme.isActive();
        Optional<Theme> target = findById(theme.id());
        if(target.isEmpty()){
            return false;
        }
        target.get().updateStatus(isActive);
        return true;
    }

    public void saveAll(List<Theme> themes) {
        themes.forEach(this::save);
    }

    private void autoIncrement() {
        autoIncrement = ++autoIncrement;
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
}
