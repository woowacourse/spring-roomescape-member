package roomescape.global.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorCode {
    INVALID_INPUT("잘못된 요청입니다."),
    AUTHENTICATION_FAILED("인증에 실패했습니다."),
    NOT_FOUND("잘못된 경로입니다."),
    BAD_REQUEST("잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    GlobalErrorCode(String message) {
        this.message = message;
    }

}
