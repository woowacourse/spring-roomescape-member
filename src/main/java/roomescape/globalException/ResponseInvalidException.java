package roomescape.globalException;

import org.springframework.http.HttpStatus;

public class ResponseInvalidException extends CustomException{
    public ResponseInvalidException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 관리자에게 문의하세요");
    }
}
