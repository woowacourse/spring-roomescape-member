package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Theme;

public record ThemeSaveRequest(@NotBlank String name, @NotNull String description, @NotNull String thumbnail) {

    public static Theme toEntity(ThemeSaveRequest request) {
        return new Theme(request.name(), request.description(), request.thumbnail());
    }
}
