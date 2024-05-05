package roomescape.exception;

import org.springframework.http.HttpStatusCode;

public class CustomException extends RuntimeException {

    private final CustomExceptionCode customExceptionCode;

    public CustomException(CustomExceptionCode customExceptionCode) {
        super(customExceptionCode.getDetail());
        this.customExceptionCode = customExceptionCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return customExceptionCode.getHttpStatusCode();
    }

    public String getDetails() {
        return customExceptionCode.getDetail();
    }
}
