package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public class CollectionThemeRepository implements ThemeRepository {
    @Override
    public List<Theme> findAll() {
        return null;
    }

    @Override
    public Optional<Theme> findById(long id) {
        return Optional.of(new Theme(id, "이름", "설명", "썸네일"));
    }

    @Override
    public Theme save(Theme theme) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
