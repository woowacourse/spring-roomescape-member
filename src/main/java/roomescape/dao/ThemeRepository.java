package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    void deleteById(long id);

    Optional<Theme> findById(long id);

    List<Theme> findByReservationTermAndCount(String startDate, String endDate, long count);
}
