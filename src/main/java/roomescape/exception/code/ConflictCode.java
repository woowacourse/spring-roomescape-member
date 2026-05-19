package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum ConflictCode implements ErrorCode {
    RESERVATION_DUPLICATED("이미 해당 날짜/시간/테마로 예약이 존재합니다. 다른 날짜, 시간, 테마로 예약을 시도해주세요."),
    RESERVATION_TIME_IN_USE("예약이 존재하는 시간은 삭제할 수 없습니다. 해당 시간의 예약을 먼저 취소해주세요."),
    THEME_IN_USE("예약이 존재하는 테마는 삭제할 수 없습니다. 해당 테마의 예약을 먼저 취소해주세요.");

    private final String message;

    ConflictCode(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
