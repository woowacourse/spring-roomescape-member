package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DATA_CONFLICT(HttpStatus.CONFLICT, "요청한 데이터가 현재 상태와 충돌합니다."),
    RESERVATION_TIME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
