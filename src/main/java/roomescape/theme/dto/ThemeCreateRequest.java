package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ThemeCreateRequest(String name, String description, String thumbnailImgUrl) {

    public Theme toEntity() {
        return Theme.create(name, description, thumbnailImgUrl);
    }
}
