package roomescape.exception;

public enum ExceptionCause {

    INVALID_JWT_INVALID_SIGNATURE("JWT 토큰이 올바르지 않습니다."),
    INVALID_JWT_EMPTY("JWT 토큰이 비어있습니다."),
    INVALID_JWT_EXPIRED("JWT 토큰이 만료되었습니다."),

    MEMBER_NOTFOUND("회원이 존재하지 않습니다."),
    MEMBER_EXIST("이미 존재하는 이메일입니다."),
    MEMBER_UNAUTHORIZED("권한이 없습니다."),
    MEMBER_PASSWORD_INVALID("비밀번호는 8자 ~ 16자이고, 대소문자 특수문자 숫자 중 3가지 이상을 포함해야합니다."),

    RESERVATION_EXIST_THEME("이 테마에 대한 예약이 존재합니다."),
    RESERVATION_EXIST_TIME("이 시간에 대한 예약이 존재합니다."),
    RESERVATION_DUPLICATE("이미 존재하는 예약입니다."),
    RESERVATION_NAME_INVALID_LENGTH("예약자명은 10자 이하여야합니다."),
    RESERVATION_IMPOSSIBLE_FOR_PAST("예약은 미래만 가능합니다."),
    RESERVATION_NOTFOUND("예약이 존재하지 않습니다."),

    RESERVATION_TIME_NOTFOUND("예약 시간이 존재하지 않습니다."),

    THEME_NOTFOUND("테마가 존재하지 않습니다."),

    EMPTY_VALUE_MEMBER_NAME("이름이 비어있습니다."),
    EMPTY_VALUE_MEMBER_EMAIL("이메일이 비어있습니다."),
    EMPTY_VALUE_MEMBER_PASSWORD("비밀번호가 비어있습니다."),
    EMPTY_VALUE_MEMBER_ROLE("권한이 비어있습니다."),
    EMPTY_VALUE_RESERVATION_MEMBER("예약자가 비어있습니다."),
    EMPTY_VALUE_RESERVATION_TIME("예약 시간이 비어있습니다."),
    EMPTY_VALUE_RESERVATION_DATE("예약 날짜가 비어있습니다."),
    EMPTY_VALUE_THEME("예약 테마가 비어있습니다."),
    EMPTY_VALUE_THEME_NAME("테마 명이 비어있습니다."),
    EMPTY_VALUE_THEME_DESCRIPTION("테마 설명이 비어있습니다."),
    EMPTY_VALUE_THEME_THUMBNAIL("테마 섬네일이 비어있습니다.");

    private final String message;

    ExceptionCause(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
