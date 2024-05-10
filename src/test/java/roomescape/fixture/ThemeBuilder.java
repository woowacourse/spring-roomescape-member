package roomescape.fixture;

import roomescape.domain.Theme;

public class ThemeBuilder {
    public static final Theme DEFAULT_THEME = new Theme(1L, "theme", "description", "https://thumbnail.com");

    public static Theme from(String name) {
        return new Theme(name, "description", "https://thumbnail.com");
    }
}
