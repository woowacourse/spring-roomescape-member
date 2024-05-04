package roomescape.dto.request;

import roomescape.domain.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
    public Theme toEntity() {
        return Theme.builder()
                .name(this.name)
                .description(this.description)
                .thumbnail(thumbnail)
                .build();
    }
}
