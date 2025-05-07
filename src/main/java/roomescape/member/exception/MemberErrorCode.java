package roomescape.member.exception;

import roomescape.global.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    MEMBER_FIELD_REQUIRED("M001", "필수 입력 값이 누락되었습니다.");

    private final String code;
    private final String message;

    MemberErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
