package roomescape.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.domain.ThemeSortType;
import roomescape.global.exception.BusinessException;

public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Theme save(Theme theme) {
        if (theme.id() == null) {
            Theme saved = Theme.createRow(++sequence, theme.name(), theme.description(), theme.thumbnailUrl());
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
            throw new BusinessException(HttpStatus.NOT_FOUND, "테마를 찾을 수 없습니다.");
        }
    }

    public void clear() {
        store.clear();
        sequence = 0L;
    }
}
