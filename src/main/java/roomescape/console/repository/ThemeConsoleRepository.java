package roomescape.console.repository;

import java.util.List;
import roomescape.core.domain.Theme;
import roomescape.core.repository.ThemeRepository;

public class ThemeConsoleRepository implements ThemeRepository {

    @Override
    public Long save(final Theme theme) {
        return null;
    }

    @Override
    public List<Theme> findAll() {
        return null;
    }

    @Override
    public List<Theme> findPopular() {
        return null;
    }

    @Override
    public Theme findById(final long id) {
        return null;
    }

    @Override
    public Integer countByName(final String name) {
        return null;
    }

    @Override
    public void deleteById(final long id) {
    }
}
