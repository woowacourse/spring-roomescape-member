package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

public interface ReservedThemeChecker {
    boolean isReservedTheme(Long themeId);

    List<Long> getBestThemesIdInDays(LocalDate startDate, LocalDate endDate);
}
