package roomescape.service.dto;

import roomescape.domain.Theme;

public record SaveThemeRequest(String name, String description, String thumbnail) {

    public static Theme toEntity(SaveThemeRequest request) {
        return new Theme(request.name(), request.description(), request.thumbnail());
    }
}
