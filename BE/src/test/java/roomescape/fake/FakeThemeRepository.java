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
import roomescape.global.exception.EntityNotFoundException;

public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Theme save(Theme theme) {
        if (theme.getId() == null) {
            Theme saved = Theme.createRow(++sequence, theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
            store.put(saved.getId(), saved);
            return saved;
        }

        store.put(theme.getId(), theme);

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
    public void deleteById(Long id) {
        Theme removed = store.remove(id);
        if (removed == null) {
            throw new EntityNotFoundException("테마를 찾을 수 없습니다.");
        }
    }

    public void clear() {
        store.clear();
        sequence = 0L;
    }
}
