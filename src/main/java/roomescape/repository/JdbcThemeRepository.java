package roomescape.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    @Override
    public List<Theme> findAll() {
        return List.of();
    }

    @Override
    public Theme findById(long id) {
        return null;
    }

    @Override
    public Theme save(Theme theme) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}
