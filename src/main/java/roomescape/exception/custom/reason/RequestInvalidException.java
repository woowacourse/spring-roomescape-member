package roomescape.exception.custom.reason;

import org.springframework.http.HttpStatus;
import roomescape.exception.custom.status.CustomException;

public class RequestInvalidException extends CustomException {

    public RequestInvalidException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다.");
    }
}
