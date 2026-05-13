package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 테마
    THEME_NAME_BLANK(HttpStatus.BAD_REQUEST, "테마 이름은 빈값일 수 없습니다.", "name에 값을 채워서 다시 요청해주세요."),
    THEME_NAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "테마 이름의 길이가 유효하지 않습니다.", "name 필드의 값을 1~20자로 맞춰 보내주세요."),
    THEME_DESCRIPTION_BLANK(HttpStatus.BAD_REQUEST, "테마 설명은 빈값일 수 없습니다.", "description에 값을 채워서 다시 요청해주세요."),
    THEME_DESCRIPTION_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "테마 설명의 길이가 유효하지 않습니다.",
            "description 필드의 값을 1~1000자로 맞춰 보내주세요."),
    THEME_URL_BLANK(HttpStatus.BAD_REQUEST, "테마 URL은 빈값일 수 없습니다.", "url에 값을 채워서 다시 요청해주세요."),
    THEME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 테마입니다.", null),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다.", null),
    THEME_HAS_RESERVATIONS(HttpStatus.CONFLICT, "예약이 존재하는 테마는 삭제할 수 없습니다.", null),

    // 예약 시간
    TIME_NULL(HttpStatus.BAD_REQUEST, "예약 시작 시간은 필수입니다.", "startAt에 값을 채워서 다시 요청해주세요."),
    TIME_NOT_ON_THE_HOUR(HttpStatus.UNPROCESSABLE_ENTITY, "예약 시작 시간은 정각이어야 합니다.",
            "startAt 값을 정각(HH:00 형식)으로 맞춰 보내주세요."),
    TIME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 예약 시간입니다.", null),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다.", null),
    TIME_HAS_RESERVATIONS(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다.", null),

    // 예약
    RESERVATION_NAME_BLANK(HttpStatus.BAD_REQUEST, "예약자 이름은 빈값일 수 없습니다.", "name에 값을 채워서 다시 요청해주세요."),
    RESERVATION_NAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "예약자 이름의 길이가 유효하지 않습니다.", "name 필드의 값을 2~20자로 맞춰 보내주세요."),
    RESERVATION_DATE_NULL(HttpStatus.BAD_REQUEST, "예약 날짜는 필수입니다.", "date에 값을 채워서 다시 요청해주세요."),
    RESERVATION_TIME_NULL(HttpStatus.BAD_REQUEST, "예약 시간은 필수입니다.", "timeId에 값을 채워서 다시 요청해주세요."),
    RESERVATION_THEME_NULL(HttpStatus.BAD_REQUEST, "예약 테마는 필수입니다.", "themeId에 값을 채워서 다시 요청해주세요."),
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 예약입니다.", "다른 테마, 날짜, 시간으로 예약을 시도해주세요."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 시간입니다.", null),
    RESERVATION_THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다.", null),
    RESERVATION_PAST_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 날짜·시간으로는 예약할 수 없습니다.", null),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다.", null),
    RESERVATION_PAST_UPDATE(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 예약은 변경할 수 없습니다.", null),
    RESERVATION_PAST_CANCEL(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 예약은 취소할 수 없습니다.", null),
    RESERVATION_TIME_ALREADY_BOOKED(HttpStatus.CONFLICT, "변경하려는 날짜·시간에 이미 예약이 존재합니다.", "다른 날짜·시간으로 변경을 시도해주세요.");

    private final HttpStatus status;
    private final String message;
    private final String action;

    ErrorCode(HttpStatus status, String message, String action) {
        this.status = status;
        this.message = message;
        this.action = action;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return name();
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }
}
