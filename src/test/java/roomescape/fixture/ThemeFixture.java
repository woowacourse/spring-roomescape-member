package roomescape.fixture;

import roomescape.business.domain.theme.Theme;
import roomescape.business.domain.theme.ThemeDescription;
import roomescape.business.domain.theme.ThemeName;
import roomescape.business.domain.theme.ThemeThumbnail;

public class ThemeFixture {

    public static final Theme THEME = new Theme(
            1L,
            new ThemeName("테마명1"),
            new ThemeDescription("이것은 테마1 입니다."),
            new ThemeThumbnail("썸네일1")
    );
}
