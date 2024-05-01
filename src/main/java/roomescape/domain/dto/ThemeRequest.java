package roomescape.domain.dto;

import roomescape.domain.Theme;

public record ThemeRequest(String name, String description, String thumbnail) {
    //TODO: 예외 처리
    public Theme toEntity(final Long id) {
        return new Theme(id, name, description, thumbnail);
    }
}
