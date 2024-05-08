package roomescape.fixture;

import roomescape.domain.theme.Theme;

public class ThemeFixture {
    public static final Theme DEFAULT_THEME = theme("셜록홈즈");

    public static Theme theme(String name) {
        return new Theme(1L, name, "설명입니다.", "https://lemone.com");
    }
}
