package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum ConflictCode implements ErrorCode {
    RESERVATION_DUPLICATED("이미 존재하는 예약입니다.", "다른 테마, 날짜, 시간으로 예약을 시도해주세요."),
    RESERVATION_TIME_IN_USE("예약이 존재하는 시간은 삭제할 수 없습니다.", "해당 시간의 예약을 먼저 취소해주세요.");

    private final String message;
    private final String action;

    ConflictCode(String message, String action) {
        this.message = message;
        this.action = action;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
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
