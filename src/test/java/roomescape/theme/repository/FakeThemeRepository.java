package roomescape.theme.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import roomescape.theme.model.Theme;

@Repository @Qualifier("FakeThemeRepository")
public class FakeThemeRepository implements ThemeRepository {
    private final List<Theme> themes = new ArrayList<>();

    @Override
    public Theme save(final Theme theme) {
        Theme newTheme = Theme.of((long) themes.size() + 1, theme);
        themes.add(newTheme);
        return newTheme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        int index = id.intValue() - 1;
        if (themes.size() > index) {
            return Optional.of(themes.get(index));
        }
        return Optional.empty();
    }

    @Override
    public List<Theme> findOrderByReservation() {
        return themes.stream()
                .sorted()
                .toList();
    }

    @Override
    public boolean existsById(final Long id) {
        return themes.stream()
                .anyMatch(theme -> theme.isSameTo(id));
    }

    @Override
    public void deleteById(final Long id) {
        themes.remove(id.intValue() - 1);
    }
}
