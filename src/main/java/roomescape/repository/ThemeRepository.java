package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    void save(Theme theme);

    void delete(Long id);

    Theme findById(Long id);

    List<Theme> calculateRankForReservationAmount(LocalDate startDate, LocalDate currentDate);
}
