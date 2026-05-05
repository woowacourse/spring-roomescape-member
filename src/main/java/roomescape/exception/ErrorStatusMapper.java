package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ErrorStatusMapper {

    private final Map<ErrorCode, HttpStatus> statusByErrorCode = new EnumMap<>(ErrorCode.class);

    public ErrorStatusMapper() {
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_ID, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_NAME, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_DATE, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_TIME, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.INVALID_RESERVATION_TIME_ID, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.RESERVATION_ALREADY_HAS_ID, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_ALREADY_HAS_ID, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.RESERVATION_CREATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_NOT_FOUND, HttpStatus.NOT_FOUND);
        statusByErrorCode.put(ErrorCode.RESERVATION_TIME_CREATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        statusByErrorCode.put(ErrorCode.INVALID_THEME, HttpStatus.BAD_REQUEST);
        statusByErrorCode.put(ErrorCode.THEME_NOT_FOUND, HttpStatus.NOT_FOUND);
        statusByErrorCode.put(ErrorCode.THEME_CREATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus map(ErrorCode errorCode) {
        return Optional.ofNullable(statusByErrorCode.get(errorCode))
                .orElseThrow(() -> new IllegalStateException("매핑되지 않은 에러 코드입니다: " + errorCode));
    }
}
