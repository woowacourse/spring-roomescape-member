package roomescape.repository;

import roomescape.entity.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeDao {

    List<Theme> findAll();

    Theme save(Theme theme);

    Optional<Theme> findById(Long id);

    void deleteById(Long id);
}
