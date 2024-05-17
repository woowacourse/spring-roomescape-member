package roomescape.domain.reservation;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    void deleteById(long id);

    boolean existsByName(String name);

    Optional<Theme> findById(long id);

    Theme getById(Long id);

    List<Theme> findByReservationTermAndCount(String startDate, String endDate, long count);
}
