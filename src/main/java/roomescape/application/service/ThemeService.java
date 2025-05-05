package roomescape.application.service;

import java.util.List;
import roomescape.application.dto.ThemeRequest;
import roomescape.application.dto.ThemeResponse;

public interface ThemeService {

    List<ThemeResponse> findAllThemes();

    List<ThemeResponse> findThemeRank();

    ThemeResponse createTheme(ThemeRequest themeRequest);

    void deleteTheme(long id);
}
