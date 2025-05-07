package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    Theme add(Theme theme);

    List<Theme> findAll();

    Theme findById(Long id);

    List<Theme> findMostReservedThemesInPeriodWithLimit(LocalDate startDate, LocalDate endDate, int limitCount);

    int deleteById(Long id);

    boolean existByName(String name);
}
