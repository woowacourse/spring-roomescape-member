package roomescape.service.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

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
    public Long insert(Theme theme) {
        Long id = ++sequence;
        store.put(id, new Theme(id, theme.getName(), theme.getThumbnailUrl(), theme.getDescription()));
        return id;
    }

    @Override
    public int delete(Long id) {
        Theme remove = store.remove(id);
        if (remove == null) {
            return 0;
        }
        return 1;
    }
}
