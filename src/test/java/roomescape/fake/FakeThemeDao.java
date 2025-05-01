package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

public class FakeThemeDao implements ThemeDao {

    private final List<Theme> themes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Theme> findAllThemes() {
        return new ArrayList<>(themes);
    }

    @Override
    public Theme addTheme(Theme theme) {
        Theme saved = new Theme(
            index.getAndIncrement(),
            theme.getName(),
            theme.getDescription(),
            theme.getThumbnail()
        );
        themes.add(saved);
        return saved;
    }

    @Override
    public void removeThemeById(Long id) {
        themes.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Theme findThemeById(Long id) {
        return themes.stream()
            .filter(theme -> theme.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
    }

    @Override
    public List<Theme> findTopReservedThemesInPeriodWithLimit(LocalDate startDate,
        LocalDate endDate, int limitCount) {
        return List.of();
    }

    @Override
    public boolean existThemeByName(String name) {
        return false;
    }
}
