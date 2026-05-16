package roomescape.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.code.RoomEscapeErrorCode;

public final class RoomEscapeErrorCodeHttpStatusMapper {

    private RoomEscapeErrorCodeHttpStatusMapper() {
    }

    public static HttpStatus getHttpStatus(RoomEscapeErrorCode roomEscapeErrorCode) {
        return switch (roomEscapeErrorCode) {
            case RESERVATION_DATE_IN_PAST,
                 RESERVATION_TIME_IN_PAST,
                 RESERVATION_NOT_CHANGED -> HttpStatus.UNPROCESSABLE_ENTITY;

            case RESERVATION_ALREADY_RESERVED,
                 RESERVATION_TIME_ALREADY_USED,
                 RESERVATION_TIME_ALEADY_EXISTS -> HttpStatus.CONFLICT;

            case RESERVATION_NOT_FOUND,
                 THEME_NOT_FOUND,
                 RESERVATION_TIME_NOT_FOUND -> HttpStatus.NOT_FOUND;
        };
    }
}