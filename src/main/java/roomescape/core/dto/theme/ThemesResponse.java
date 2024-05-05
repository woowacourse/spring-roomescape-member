package roomescape.core.dto.theme;

import java.util.List;

public class ThemesResponse {
    private List<ThemeResponse> themes;

    public ThemesResponse() {
    }

    public ThemesResponse(final List<ThemeResponse> themes) {
        this.themes = themes;
    }

    public List<ThemeResponse> getThemes() {
        return themes;
    }
}
