package roomescape.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.entity.ThemeSortType;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ThemeException;


public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Theme save(Theme theme) {
        if (theme.id() == null) {
            Theme saved = Theme.createWithId(++sequence, theme.name(), theme.description(), theme.thumbnailUrl());
            store.put(saved.id(), saved);
            return saved;
        }

        store.put(theme.id(), theme);

        return theme;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Theme> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit) {
        long maxResults = (limit != null) ? limit : 10L;

        return store.values().stream()
                .sorted((t1, t2) -> Long.compare(t2.id(), t1.id()))
                .limit(maxResults)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Theme removed = store.remove(id);
        if (removed == null) {
            throw new ThemeException(ErrorCode.THEME_NOT_FOUND);
        }
    }

    public void clear() {
        store.clear();
        sequence = 0L;
    }
}
