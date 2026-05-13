package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ALREADY_EXISTS_THEME("THEME_409_1","존재하는 테마는 추가할 수 없습니다.",HttpStatus.CONFLICT),
    UNALLOWED_DELETE_EXISTS_THEME("THEME_409_2","사용중인 테마는 삭제할 수 없습니다.",HttpStatus.CONFLICT),
    ALREADY_EXISTS_TIME("TIME_409_1", "이미 존재하는 시간은 저장할 수 없습니다.", HttpStatus.CONFLICT),
    UNALLOWED_DELETE_RESERVED_TIME("TIME_409_2", "예약중인 시간은 삭제할 수 없습니다.", HttpStatus.CONFLICT),
    DUPLICATE_RESERVATION("RESERVATION_409_1", "중복 예약이 불가능합니다.", HttpStatus.CONFLICT),
    PAST_DATE_RESERVATION("RESERVATION_400_1", "과거 날짜/시간은 예약할 수 없습니다. ", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
