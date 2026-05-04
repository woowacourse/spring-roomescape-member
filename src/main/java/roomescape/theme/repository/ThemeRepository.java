package roomescape.theme.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    boolean deleteById(long id);
}