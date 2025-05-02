package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme add(Theme theme);

    int deleteById(Long id);

    Theme findById(Long id);

    List<Theme> findMostReservedThemesInPeriodWithLimit(LocalDate startDate, LocalDate endDate, int limitCount);

    boolean existByName(String name);
}
