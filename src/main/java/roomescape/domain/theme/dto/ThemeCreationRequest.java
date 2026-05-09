package roomescape.domain.theme.dto;

import roomescape.domain.theme.Theme;

public record ThemeCreationRequest(
    String name,
    String content,
    String url
) {

    public Theme toEntity() {
        return Theme.createWithoutId(
            name,
            content,
            url
        );
    }
}
