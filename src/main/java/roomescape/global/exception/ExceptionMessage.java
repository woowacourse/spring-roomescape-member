package roomescape.global.exception;

public enum ExceptionMessage {

    THEME_NOT_FOUND("[ERROR] 테마를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("[ERROR] 사용자를 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND("[ERROR] 예약 시간을 찾을 수 없습니다."),
    COOKIE_NOT_FOUND("[ERROR] 쿠키가 존재하지 않습니다."),
    TOKEN_NOT_FOUND("[ERROR] 토큰이 존재하지 않습니다."),
    REQUEST_NOT_FOUND("[ERROR] 요청이 존재하지 않습니다."),
    AUTHENTICATION_NOT_FOUND("[ERROR] 권한 정보가 존재하지 않습니다."),
    CANNOT_ACCESS("[ERROR] 접근 권한이 없습니다."),
    DATE_CANNOT_NULL("[ERROR] 날짜는 비어있을 수 없습니다."),
    TIME_CANNOT_NULL("[ERROR] 시간은 비어있을 수 없습니다."),
    THEME_CANNOT_NULL("[ERROR] 테마는 비어있을 수 없습니다."),
    MEMBER_CANNOT_NULL("[ERROR] 멤버는 비어있을 수 없습니다."),
    EMAIL_CANNOT_NULL("[ERROR] 이메일은 비어있을 수 없습니다."),
    PASSWORD_CANNOT_NULL("[ERROR] 비밀번호는 비어있을 수 없습니다."),
    NAME_CANNOT_NULL("[ERROR] 이름은 비어있을 수 없습니다."),
    START_AT_CANNOT_NULL("[ERROR] 시작 시간은 비어있을 수 없습니다."),
    DESCRIPTION_CANNOT_NULL("[ERROR] 설명은 비어있을 수 없습니다."),
    THUMBNAIL_CANNOT_NULL("[ERROR] 썸네일은 비어있을 수 없습니다."),
    TIME_ALREADY_PAST("[ERROR] 이미 지난 시간입니다."),
    RESERVATION_ALREADY_EXIST("[ERROR] 이미 예약이 존재합니다."),
    TIME_EXIST_RESERVATION_CANNOT_DELETE("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다."),
    THEME_EXIST_RESERVATION_CANNOT_DELETE("[ERROR] 예약이 존재하는 시간은 삭제할 수 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
