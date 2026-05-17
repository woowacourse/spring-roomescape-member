package roomescape.controller.dto;

import roomescape.exception.RoomescapeException;

public record ErrorResponse(
        String code,
        String detail
) {

    public static ErrorResponse from(RoomescapeException exception) {
        return new ErrorResponse(exception.getErrorCode().name(), exception.getMessage());
    }
}
