package roomescape.support.exception;

import lombok.Getter;

@Getter
public enum RoomescapeErrorCode implements ErrorCode {

    BAD_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다"),
    INVALID_GENERATED_KEY("생성 키를 조회할 수 없습니다."),
    ;

    private final String message;

    RoomescapeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
