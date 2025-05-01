package roomescape.domain.reservation.repository.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private static final Long INITIAL_ID = 1L;

    private final AtomicLong id = new AtomicLong(INITIAL_ID);
    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();

    @Override
    public List<Theme> findAll() {
        return themes.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        if (themes.containsKey(id)) {
            return Optional.of(themes.get(id));
        }

        throw new EntityNotFoundException("theme with id " + id + " not found");
    }

    @Override
    public List<Theme> findThemeRankingByReservation(LocalDate startDate, LocalDate endDate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Theme save(Theme theme) {
        if (theme.existId() && !themes.containsKey(theme.getId())) {
            throw new EntityNotFoundException("theme with id " + theme.getId() + " not found");
        }

        if (theme.existId()) {
            themes.put(theme.getId(), theme);
            return theme;
        }

        Theme themeWithId = new Theme(id.getAndIncrement(), theme.getName(), theme.getDescription(),
                theme.getThumbnail());
        themes.put(themeWithId.getId(), themeWithId);
        return themeWithId;
    }

    @Override
    public void deleteById(Long id) {
        if (!themes.containsKey(id)) {
            throw new EntityNotFoundException("theme with id " + id + " not found");
        }

        themes.remove(id);
    }

    public void add(Theme theme) {
        themes.put(theme.getId(), theme);
    }

    public void deleteAll() {
        themes.clear();
        id.set(INITIAL_ID);
    }
}
