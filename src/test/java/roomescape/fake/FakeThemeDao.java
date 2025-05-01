package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.repository.ThemeDao;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

public class FakeThemeDao implements ThemeDao {

    List<Theme> themes = new ArrayList<>();
    Long index = 1L;

    @Override
    public boolean isExists(final ThemeName name) {
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(name.getName()));
    }

    @Override
    public Theme save(final Theme theme) {
        Theme savedTheme = new Theme(index++, theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(savedTheme);
        return savedTheme;
    }

    @Override
    public List<Theme> findAll() {
        return themes;
    }

    @Override
    public Theme findById(final long id) {
        return themes.stream()
                .filter(theme -> theme.getId() == id)
                .findAny()
                .orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        Theme theme = findById(id);
        themes.remove(theme);
    }

    /**
     * TODO
     * JOIN을 구현할 방법 찾기
     */
    @Override
    public List<Theme> findPopularThemes(LocalDate from, LocalDate to, int count) {
        return null;
    }
}
