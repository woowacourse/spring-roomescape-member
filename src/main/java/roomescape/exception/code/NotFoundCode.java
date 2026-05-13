package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum NotFoundCode implements ErrorCode {
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다.", "예약 ID를 다시 확인해주세요."),
    RESERVATION_TIME_NOT_FOUND("예약 시간을 찾을 수 없습니다.", "예약 시간 ID를 다시 확인해주세요."),
    THEME_NOT_FOUND("테마를 찾을 수 없습니다.", "테마 ID를 다시 확인해주세요."),
    RESOURCE_NOT_FOUND("찾는 리소스가 없습니다.", "요청한 리소스 경로를 다시 확인해주세요.");

    private final String message;
    private final String action;

    NotFoundCode(String message, String action) {
        this.message = message;
        this.action = action;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
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
