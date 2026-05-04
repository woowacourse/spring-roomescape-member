package roomescape.theme.service;

import roomescape.theme.domain.Theme;

public interface ThemeService {

    Theme createTheme(String name, String description, String thumbnail);
}
