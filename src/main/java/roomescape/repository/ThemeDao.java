package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeDao {
    Theme save(final Theme theme);

    List<Theme> getAll();

    Optional<Theme> findById(final long themeId);

    void delete(final long id);
}
