package roomescape.domain.theme.dto.request;

import roomescape.domain.theme.validator.ThemeCreateRequestValidator;

public record ThemeCreateRequestDto(String name, String description, String imageUrl) {

    public ThemeCreateRequestDto {
        ThemeCreateRequestValidator.validate(name, description, imageUrl);
    }

}
