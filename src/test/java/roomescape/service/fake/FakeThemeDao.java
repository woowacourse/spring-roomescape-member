package roomescape.service.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.response.AvailableTimeResponseDto;

public class FakeThemeDao implements ThemeDao {
    private final Map<Long, Theme> store = new HashMap<>();

    private long sequence = 0L;


    @Override
    public List<Theme> findAll() {
        return store.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        Theme theme = store.get(id);

        if (theme == null) {
            return Optional.empty();
        }
        return Optional.of(theme);
    }

    @Override
    public Theme insert(Theme theme) {
        Long id = ++sequence;
        Theme newTheme = new Theme(id, theme.getName(), theme.getThumbnailUrl(), theme.getDescription());
        store.put(id, newTheme);
        return newTheme;
    }

    @Override
    public int delete(Long id) {
        Theme remove = store.remove(id);
        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsByName(Name name) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }

    @Override
    public List<AvailableTimeResponseDto> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return List.of();
    }

    @Override
    public List<Theme> findPopulars(PopularThemeRequestDto popularThemeRequestDto) {
        return List.of();
    }
}
