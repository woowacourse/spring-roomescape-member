package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.domain.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Theme> findAll() {
        return themes.entrySet().stream()
                .map(entry -> {
                    Theme value = entry.getValue();
                    return Theme.of(entry.getKey(), value.getName(), value.getDescription(), value.getThumbnail());
                })
                .toList();
    }

    @Override
    public Theme save(final Theme theme) {
        Long id = index.getAndIncrement();
        themes.put(id, theme);
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final Long id) {
        return themes.remove(id) != null;
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public List<Theme> findTop10PopularThemesWithinLastWeek(final LocalDate nowDate) {
        // 예약 중에 가장 인기있는 테마 찾기
        Theme theme1 = Theme.of(1L, "name", "des", "thumbnail");
        Theme theme2 = Theme.of(2L, "name", "des", "thumbnail");
        Theme theme3 = Theme.of(3L, "name", "des", "thumbnail");
        Theme theme4 = Theme.of(4L, "name", "des", "thumbnail");
        Theme theme5 = Theme.of(5L, "name", "des", "thumbnail");
        Theme theme6 = Theme.of(6L, "name", "des", "thumbnail");
        Theme theme7 = Theme.of(7L, "name", "des", "thumbnail");
        Theme theme8 = Theme.of(8L, "name", "des", "thumbnail");
        Theme theme9 = Theme.of(9L, "name", "des", "thumbnail");
        Theme theme10 = Theme.of(10L, "name", "des", "thumbnail");
        return List.of(theme1, theme2, theme3, theme4, theme5, theme6, theme7, theme8, theme9, theme10);
    }
}
