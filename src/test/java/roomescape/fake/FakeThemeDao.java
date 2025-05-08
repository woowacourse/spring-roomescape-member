package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ThemeDao;
import roomescape.persistence.entity.ThemeEntity;

public class FakeThemeDao implements ThemeDao {

    private final List<ThemeEntity> themes;

    private int index = 1;

    public FakeThemeDao() {
        this.themes = new ArrayList<>();
        final ThemeEntity dummy = new ThemeEntity(null, null, null, null);
        themes.add(dummy);
    }

    public FakeThemeDao(final List<ThemeEntity> themes) {
        this.themes = themes;
        index += themes.size();
        final ThemeEntity dummy = new ThemeEntity(null, null, null, null);
        themes.addFirst(dummy);
    }

    @Override
    public Long save(final Theme theme) {
        final ThemeEntity temp = ThemeEntity.from(theme);
        final ThemeEntity themeEntity = new ThemeEntity(
                (long) index,
                temp.name(),
                temp.description(),
                temp.thumbnail()
        );
        themes.add(index, themeEntity);

        return (long) index++;
    }

    @Override
    public Optional<Theme> find(final Long id) {
        try {
            final ThemeEntity themeEntity = themes.get(Math.toIntExact(id));
            return Optional.of(themeEntity.toDomain());
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        return themes.stream()
                .filter(themeEntity -> themeEntity.id() != null)
                .map(ThemeEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        try {
            themes.remove(themes.get(Math.toIntExact(id)));
            index--;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public List<Theme> findPopularThemesBetweenWithLimit(
            final String startDate,
            final String endDate,
            final int limit
    ) {
        final Map<Long, Integer> reservations = Map.of(
                1L, 5,
                2L, 3,
                3L, 10,
                4L, 2
        );

        return themes.stream()
                .filter(themeEntity -> themeEntity.id() != null)
                .map(ThemeEntity::toDomain)
                .sorted((t1, t2) -> {
                    int count1 = reservations.getOrDefault(t1.getId(), 0);
                    int count2 = reservations.getOrDefault(t2.getId(), 0);

                    if (count1 == count2) {
                        return Long.compare(t1.getId(), t2.getId());
                    }
                    return Integer.compare(count2, count1);
                })
                .limit(limit)
                .toList();
    }
}
