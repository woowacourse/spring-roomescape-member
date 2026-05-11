package roomescape.common.exception;

import org.springframework.http.HttpStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

public class ErrorStatusMapper {

    public static final Map<ErrorCode, HttpStatus> statusByErrorCode = new EnumMap<>(ErrorCode.class);

    static {
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_ID, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_NAME, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_DATE, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_TIME, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_TIME_ID, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_THEME_ID, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_THEME_NAME, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_THEME_DESCRIPTION, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_THEME_THUMBNAIL, BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_THEME, BAD_REQUEST);

        statusByErrorCode.put(ErrorCode.RESERVATION_NOT_FOUND, NOT_FOUND);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_NOT_FOUND, NOT_FOUND);
        statusByErrorCode.put(ErrorCode.THEME_NOT_FOUND, NOT_FOUND);

        statusByErrorCode.put(ErrorCode.RESERVATION_ALREADY_HAS_ID, CONFLICT);
        statusByErrorCode.put(ErrorCode.RESERVATION_ALREADY_EXISTS, CONFLICT);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_ALREADY_HAS_ID, CONFLICT);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_ALREADY_EXISTS, CONFLICT);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_HAS_RESERVATION, CONFLICT);
        statusByErrorCode.put(ErrorCode.THEME_ALREADY_HAS_ID, CONFLICT);
        statusByErrorCode.put(ErrorCode.THEME_HAS_RESERVATION, CONFLICT);

        statusByErrorCode.put(ErrorCode.PAST_RESERVATION_NOT_ALLOWED, UNPROCESSABLE_ENTITY);

        statusByErrorCode.put(ErrorCode.RESERVATION_CREATE_FAILED, INTERNAL_SERVER_ERROR);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_CREATE_FAILED, INTERNAL_SERVER_ERROR);
        statusByErrorCode.put(ErrorCode.THEME_CREATE_FAILED, INTERNAL_SERVER_ERROR);
    }

    public static HttpStatus map(ErrorCode errorCode) {
        return Optional.ofNullable(statusByErrorCode.get(errorCode))
                .orElseThrow(() -> new IllegalStateException("매핑되지 않은 에러 코드입니다: " + errorCode));
    }
}
