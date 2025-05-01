package roomescape.globalException;

import org.springframework.http.HttpStatus;

public class RequestInvalidException extends CustomException {

    public RequestInvalidException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다.");
    }
}
