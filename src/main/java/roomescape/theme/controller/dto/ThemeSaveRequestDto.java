package roomescape.theme.controller.dto;

import roomescape.theme.service.dto.ThemeSaveServiceDto;

public record ThemeSaveRequestDto(String name, String description, String imageUrl) {

    public ThemeSaveServiceDto toServiceDto() {
        return new ThemeSaveServiceDto(name, description, imageUrl);
    }
}
