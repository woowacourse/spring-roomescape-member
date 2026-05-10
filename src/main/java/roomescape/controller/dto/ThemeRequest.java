package roomescape.controller.dto;

import roomescape.controller.exception.InvalidRequestException;
import roomescape.service.dto.ThemeCreateCommand;

public record ThemeRequest(
        String name,
        String description,
        String thumbnailUrl
) {
    public ThemeRequest {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("테마 이름은 필수입니다");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidRequestException("테마 설명은 필수입니다");
        }
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new InvalidRequestException("테마 썸네일은 필수입니다");
        }
    }

    public ThemeCreateCommand toCommand() {
        return new ThemeCreateCommand(name, description, thumbnailUrl);
    }
}