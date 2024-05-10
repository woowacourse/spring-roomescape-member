package roomescape.web.dto;

import java.util.List;

public class ThemeListResponse {
    private List<?> themes;

    private ThemeListResponse() {
    }

    public ThemeListResponse(List<?> themes) {
        this.themes = themes;
    }

    public List<?> getThemes() {
        return themes;
    }
}
