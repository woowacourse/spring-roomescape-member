package roomescape.theme.controller.dto;

import roomescape.theme.service.dto.CreateThemeServiceRequest;

public record CreateThemeRequest(String name, String description, String thumbnail) {

    public CreateThemeServiceRequest toCreateThemeServiceRequest() {
        return new CreateThemeServiceRequest(name, description, thumbnail);
    }
}
