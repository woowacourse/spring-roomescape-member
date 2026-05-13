package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum BadRequestCode implements ErrorCode {
    INVALID_REQUEST("요청 값이 유효하지 않습니다.", "요청 값을 정정 후 다시 시도해주세요."),
    VALIDATION_FAILED("요청 형식이 올바르지 않습니다.", "요청 값을 정정 후 다시 시도해주세요."),
    INVALID_PATH("요청 경로가 유효하지 않습니다.", "요청 경로를 정정 후 다시 시도해주세요."),
    INVALID_RESERVATION_NAME("유효하지 않은 이름입니다.", "이름을 다시 확인해주세요."),
    BLANK_RESERVATION_NAME("이름은 공백일 수 없습니다.", "이름을 다시 확인해주세요."),
    INVALID_RESERVATION_DATE("유효하지 않은 날짜입니다.", "날짜 형식을 다시 확인해주세요."),
    INVALID_RESERVATION_TIME("유효하지 않은 시간입니다.", "시간을 다시 확인해주세요."),
    INVALID_RESERVATION_THEME("유효하지 않은 테마입니다.", "테마를 다시 확인해주세요.");

    private final String message;
    private final String action;

    BadRequestCode(String message, String action) {
        this.message = message;
        this.action = action;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getAction() {
        return action;
    }
}
