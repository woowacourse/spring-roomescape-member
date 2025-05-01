package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ThemeDao;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ThemeEntity;

public class FakeThemeDao implements ThemeDao {

    private final List<ThemeEntity> themes;

    private int index = 1;

    public FakeThemeDao() {
        this.themes = new ArrayList<>();
        final ThemeEntity dummy = new ThemeEntity(null, null, null, null);
        themes.add(dummy);
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
}
