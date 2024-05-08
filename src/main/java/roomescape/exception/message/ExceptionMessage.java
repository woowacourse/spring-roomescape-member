package roomescape.exception.message;

import org.springframework.http.HttpStatus;

public enum ExceptionMessage {

    DUPLICATE_DATE_TIME("이미 해당 날짜, 시간에 예약이 존재합니다.", HttpStatus.CONFLICT),
    DUPLICATE_THEME("이미 해당 테마가 존재합니다.", HttpStatus.CONFLICT),
    DUPLICATE_TIME("이미 해당 시간이 존재합니다.", HttpStatus.CONFLICT),
    INVALID_USER_NAME("예약자명이 null 이거나 공백인 경우 저장을 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_THEME_NAME("테마 이름이 null 이거나 공백인 경우 저장을 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PAST_DATE_RESERVATION("날짜가 과거인 경우 모든 시간에 대한 예약이 불가능 합니다.", HttpStatus.BAD_REQUEST),
    PAST_TIME_RESERVATION("날짜가 오늘인 경우 지나간 시간에 대한 예약이 불가능 합니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_THEME("해당 themeId와 일치하는 테마가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_TIME("해당 timeId와 일치하는 시간이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXIST_REFER_THEME("해당 테마를 예약한 예약내역이 존재하여 삭제가 불가합니다.", HttpStatus.BAD_REQUEST),
    EXIST_REFER_TIME("해당 시간을 예약한 예약내역이 존재하여 삭제가 불가합니다.", HttpStatus.BAD_REQUEST),
    FAIL_PARSE_DATE("형식에 맞지 않은 날짜입니다.", HttpStatus.BAD_REQUEST),
    FAIL_PARSE_TIME("형식에 맞지 않은 시간입니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;

    ExceptionMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
