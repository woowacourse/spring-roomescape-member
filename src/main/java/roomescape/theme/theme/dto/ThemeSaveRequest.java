package roomescape.theme.theme.dto;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

public record ThemeSaveRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ThemeSaveRequest {
        try {
            Objects.requireNonNull(name, "[ERROR] 이름은 null일 수 없습니다.");
            Objects.requireNonNull(description, "[ERROR] 설명은 null일 수 없습니다.");
            Objects.requireNonNull(thumbnail, "[ERROR] 썸네일은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }
}
