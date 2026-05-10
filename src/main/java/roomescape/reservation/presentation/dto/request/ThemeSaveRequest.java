package roomescape.reservation.presentation.dto.request;

import roomescape.reservation.domain.Theme;

public record ThemeSaveRequest(
        String name,
        String description,
        String thumbnailUrl
) {
    public Theme toDomain() {
        return new Theme(
                null,
                this.name,
                this.description,
                this.thumbnailUrl
        );
    }
}
