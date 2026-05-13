package roomescape.exception.code;

import org.springframework.http.HttpStatus;

public enum UnprocessableCode implements ErrorCode {
    RESERVATION_PAST_DATE("지난 날짜는 예약할 수 없습니다.", "오늘 이후의 날짜로 예약해주세요."),
    RESERVATION_PAST_TIME("지난 시간은 예약할 수 없습니다.", "현재 시각 이후의 시간으로 예약해주세요."),
    RESERVATION_ALREADY_STARTED("이미 지난 예약은 변경하거나 취소할 수 없습니다.", "변경 가능한 예약을 다시 선택해주세요.");

    private final String message;
    private final String action;

    UnprocessableCode(String message, String action) {
        this.message = message;
        this.action = action;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
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
