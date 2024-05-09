package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ThemeOutput;

public record ThemesResponse(List<ThemeResponse> themes) {

    public static ThemesResponse from(final List<ThemeOutput> outputs) {
        return new ThemesResponse(ThemeResponse.list(outputs));
    }
}
