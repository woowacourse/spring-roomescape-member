package roomescape.repository.mock;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

public class InMemoryThemeDao implements ThemeDao {

    public List<Theme> themes;

    public InMemoryThemeDao() {
        this.themes = new ArrayList<>();
    }

    @Override
    public List<Theme> findAll() {
        return themes;
    }

    @Override
    public Theme findById(final long id) {
        return themes.stream()
                .filter(theme -> theme.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }

    @Override
    public List<Theme> findThemesByDescOrder() {
        return List.of();
    }

    @Override
    public long save(final Theme theme) {
        themes.add(theme);
        return themes.size();
    }

    @Override
    public boolean existByName(final String name) {
        return themes.stream()
                .anyMatch(target -> target.getName().equals(name));
    }

    @Override
    public void deleteById(final Long id) {
        Theme theme = themes.stream()
                .filter(target -> target.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));

        themes.remove(theme);
    }
}
