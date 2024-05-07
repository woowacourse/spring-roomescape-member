package roomescape.fixture;

import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;

public class ThemeFixtures {

    private ThemeFixtures() {
    }

    public static Theme createDefaultTheme() {
        return new Theme(
                null,
                ThemeName.from("default"),
                ThemeDescription.from("default"),
                ThemeThumbnail.from("default")
        );
    }

    public static Theme createTheme(String name, String description, String thumbnail) {
        return new Theme(null, ThemeName.from(name), ThemeDescription.from(description),
                ThemeThumbnail.from(thumbnail));
    }

    public static ThemeCreateRequest createThemeCreateRequest(String name, String description, String thumbnail) {
        return ThemeCreateRequest.of(name, description, thumbnail);
    }

    public static ThemeResponse createThemeResponse(long id, String name, String description, String thumbnail) {
        return ThemeResponse.of(id, name, description, thumbnail);
    }
}
