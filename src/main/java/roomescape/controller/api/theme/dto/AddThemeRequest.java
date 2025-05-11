package roomescape.controller.api.theme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.model.Theme;

public record AddThemeRequest(

        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 1, max = 10, message = "이름은 1자 이상, 10자 이하여야 합니다.")
        String name,

        @NotBlank(message = "설명은 필수입니다.")
        @Size(min = 1, max = 255, message = "설명은 1자 이상, 255자 이하여야 합니다.")
        String description,

        @NotBlank(message = "썸네일은 필수입니다.")
        String thumbnail
) {

    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
