package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ThemeRepository;

public class FakeThemeDao implements ThemeRepository {
    private final Map<Long, Theme> themes = new HashMap<>();

    @Override
    public List<Theme> findAll() {
        return themes.values().stream()
                .toList();
    }

    @Override
    public Theme save(final Theme theme) {
        themes.put((long) themes.size() + 1, theme);
        return new Theme((long) themes.size(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
