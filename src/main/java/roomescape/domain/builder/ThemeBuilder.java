package roomescape.domain.builder;

import roomescape.domain.Theme;

public interface ThemeBuilder {
    ThemeBuilder themeId(long themeId);

    ThemeBuilder name(String name);

    ThemeBuilder description(String description);

    ThemeBuilder thumbnail(String thumbnail);

    Theme build();
}
