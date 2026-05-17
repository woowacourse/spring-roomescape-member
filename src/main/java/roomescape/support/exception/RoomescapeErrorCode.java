package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomescapeErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST,
        "잘못된 요청입니다.", "입력값 형식 확인 후 다시 요청해 주세요."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST,
        "요청 본문의 형식이 올바르지 않습니다.", "JSON 형식을 확인해 주세요."),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST,
        "요청 파라미터의 타입이 올바르지 않습니다.", "입력값의 타입을 확인해 주세요."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST,
        "필수 요청 파라미터가 누락되었습니다.", "누락된 파라미터가 없는지 확인해 주세요."),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST,
        "필수 경로 변수가 누락되었습니다.", "요청 URL을 확인해 주세요."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,
        "입력값이 유효하지 않습니다.", "입력값의 유효성 검사 규칙을 확인해 주세요."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST,
        "데이터 제약 조건을 위반했습니다.", "이미 존재하는 데이터거나 연관된 데이터가 있는지 확인해 주세요."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,
        "지원하지 않는 HTTP 메서드입니다.", "허용된 HTTP 메서드를 확인해 주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND,
        "요청한 리소스를 찾을 수 없습니다.", "요청 URL을 다시 확인해 주세요."),
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
