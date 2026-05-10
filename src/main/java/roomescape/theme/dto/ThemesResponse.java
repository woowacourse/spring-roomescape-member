package roomescape.theme.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import roomescape.theme.model.Theme;

import java.util.List;

public class ThemesResponse {
    private final List<ThemeResponse> themeResponses;

    public ThemesResponse(List<ThemeResponse> themeResponses) {
        this.themeResponses = themeResponses;
    }

    public static ThemesResponse from(List<Theme> themes) {
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(responses);
    }

    @JsonValue
    public List<ThemeResponse> getThemeResponses() {
        return themeResponses;
    }
}
