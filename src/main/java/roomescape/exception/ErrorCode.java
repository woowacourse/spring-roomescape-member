package roomescape.exception;

public enum ErrorCode {
    INVALID_INPUT(
            "INVALID_INPUT",
            "요청 값이 올바르지 않습니다.",
            "입력값을 확인해주세요."
    ),
    INVALID_REQUEST_BODY(
            "INVALID_REQUEST_BODY",
            "요청 형식이 올바르지 않습니다.",
            "형식을 올바르게 입력해주세요."
    ),
    UNSUPPORTED_MEDIA_TYPE(
            "UNSUPPORTED_MEDIA_TYPE",
            "지원하지 않는 요청 형식입니다.",
            "Content-Type을 확인해주세요."
    ),
    METHOD_NOT_ALLOWED(
            "METHOD_NOT_ALLOWED",
            "지원하지 않는 요청 메서드입니다.",
            "요청 메서드를 확인해주세요."
    ),
    API_NOT_FOUND(
            "API_NOT_FOUND",
            "요청한 API를 찾을 수 없습니다.",
            "요청 경로를 확인해주세요."
    ),
    RESERVATION_NOT_FOUND(
            "RESERVATION_NOT_FOUND",
            "예약을 찾을 수 없습니다.",
            "예약 정보를 다시 확인해주세요."
    ),
    RESERVATION_TIME_NOT_FOUND(
            "RESERVATION_TIME_NOT_FOUND",
            "존재하지 않는 예약 시간입니다.",
            "예약 가능한 시간을 다시 선택해주세요."
    ),
    THEME_NOT_FOUND(
            "THEME_NOT_FOUND",
            "존재하지 않는 테마입니다.",
            "테마 목록에서 다시 선택해주세요."
    ),
    RESERVATION_PAST_TIME(
            "RESERVATION_PAST_TIME",
            "지난 날짜와 시간은 예약할 수 없습니다.",
            "오늘 이후의 예약 가능 시간을 선택해주세요."
    ),
    RESERVATION_ALREADY_PAST(
            "RESERVATION_ALREADY_PAST",
            "이미 지난 예약입니다.",
            "지난 예약은 취소할 수 없습니다."
    ),
    RESERVATION_DUPLICATED(
            "RESERVATION_DUPLICATED",
            "이미 존재하는 예약입니다.",
            "다른 테마, 날짜, 시간으로 예약을 시도해주세요."
    ),
    RESERVATION_TIME_IN_USE(
            "RESERVATION_TIME_IN_USE",
            "해당 시간의 예약이 존재합니다.",
            "관련 예약을 먼저 취소한 뒤 삭제해주세요."
    ),
    THEME_IN_USE(
            "THEME_IN_USE",
            "해당 테마의 예약이 존재합니다.",
            "관련 예약을 먼저 취소한 뒤 삭제해주세요."
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 오류가 발생했습니다.",
            "잠시 후 다시 시도해주세요."
    );

    private final String code;
    private final String message;
    private final String action;

    ErrorCode(String code, String message, String action) {
        this.code = code;
        this.message = message;
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }
}
