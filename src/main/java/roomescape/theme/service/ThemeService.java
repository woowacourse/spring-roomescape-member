package roomescape.theme.service;

import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeService {

    Theme createTheme(String name, String description, String thumbnail);

    void removeTheme(Long id);

    List<Theme> getThemes();

    List<AvailableTime> getAvailableTimes(Long id, LocalDate date);
}
