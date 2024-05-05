package roomescape.domain;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme findById(Long id);

    List<Theme> findMostReservedThemesWithinDays(int day, int limit);

    Theme save(Theme theme);

    void deleteById(Long id);
}
