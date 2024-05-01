package roomescape.controller.dto.response;

import roomescape.service.dto.output.ThemeOutput;

public record ThemeCreateResponse(long id, String name, String description, String thumbnail) {

    public static ThemeCreateResponse toResponse(ThemeOutput output) {
        return new ThemeCreateResponse(output.id(), output.name(), output.description(), output.thumbnail());
    }
}
