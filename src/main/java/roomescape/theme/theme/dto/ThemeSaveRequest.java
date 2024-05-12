package roomescape.theme.theme.dto;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.*;

public record ThemeSaveRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ThemeSaveRequest {
        try {
            Objects.requireNonNull(name, NAME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(description, DESCRIPTION_CANNOT_NULL.getMessage());
            Objects.requireNonNull(thumbnail, THUMBNAIL_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }
}
