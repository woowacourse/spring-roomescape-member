package roomescape.exception;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import roomescape.exception.code.RoomEscapeErrorCode;

public final class RoomEscapeErrorCodeHttpStatusMapper {
    private static final Map<RoomEscapeErrorCode, HttpStatus> HTTP_STATUSES = createHttpStatuses();

    private RoomEscapeErrorCodeHttpStatusMapper() {
    }

    public static HttpStatus getHttpStatus(RoomEscapeErrorCode roomEscapeErrorCode) {
        return HTTP_STATUSES.getOrDefault(roomEscapeErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Map<RoomEscapeErrorCode, HttpStatus> createHttpStatuses() {
        Map<RoomEscapeErrorCode, HttpStatus> httpStatuses = new EnumMap<>(RoomEscapeErrorCode.class);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_DATE_IN_PAST, HttpStatus.UNPROCESSABLE_ENTITY);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_TIME_IN_PAST, HttpStatus.UNPROCESSABLE_ENTITY);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_ALREADY_RESERVED, HttpStatus.CONFLICT);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        httpStatuses.put(RoomEscapeErrorCode.THEME_NOT_FOUND, HttpStatus.NOT_FOUND);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_TIME_ALREADY_USED, HttpStatus.CONFLICT);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_TIME_NOT_FOUND, HttpStatus.NOT_FOUND);
        httpStatuses.put(RoomEscapeErrorCode.RESERVATION_TIME_ALEADY_EXISTS, HttpStatus.CONFLICT);
        return Collections.unmodifiableMap(httpStatuses);
    }
}
