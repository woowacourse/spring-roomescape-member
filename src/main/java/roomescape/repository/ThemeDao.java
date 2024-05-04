package roomescape.repository;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeDao {

    Theme save(final Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(final Long id);

    void deleteById(final Long id);

    List<Theme> findAllOrderByReservationCountInLastWeek();
}
