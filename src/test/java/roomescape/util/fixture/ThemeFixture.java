package roomescape.util.fixture;

import roomescape.theme.domain.Theme;

public class ThemeFixture {

    private static Long idSequence = 1L;

    public static Theme createDefault() {
        return new Theme(idSequence++, "default", "default", "/image/...");
    }

    public static Theme createByIdAndName(Long id, String name) {
        return new Theme(id, name, "default", "/image/...");
    }
}
