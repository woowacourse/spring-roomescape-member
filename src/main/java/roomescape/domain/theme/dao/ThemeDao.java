package roomescape.domain.theme.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.model.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    long save(Theme theme);

    boolean delete(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> calculateRankForReservationAmount(LocalDate startDate, LocalDate currentDate);
}
