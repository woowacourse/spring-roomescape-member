package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomescapeErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST,
        "잘못된 요청입니다.", "입력값 형식 확인 후 다시 요청해 주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
        "서버 내부 오류가 발생했습니다", null),
    INVALID_GENERATED_KEY(HttpStatus.INTERNAL_SERVER_ERROR,
        "생성 키를 조회할 수 없습니다.", null),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    RoomescapeErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
