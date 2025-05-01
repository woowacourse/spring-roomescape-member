package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Theme;

public class ThemeFakeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public Optional<Theme> findById(final long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public long save(final Theme theme) {
        var id = index.getAndIncrement();
        var created = new Theme(id, theme.name(), theme.description(), theme.thumbnail());
        themes.put(id, created);
        return id;
    }

    @Override
    public boolean removeById(final long id) {
        Theme removed = themes.remove(id);
        return removed != null;
    }

    @Override
    public List<Theme> findAll() {
        return List.copyOf(themes.values());
    }

    // TODO : 메서드 책임 위치
    @Override
    public List<Theme> findRankingByPeriod(final LocalDate startDate, final LocalDate endDate, final int limit) {
        return List.of();
    }
}
