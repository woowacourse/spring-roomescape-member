package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ClientIllegalArgumentException.class)
    public ResponseEntity<String> handleClientIllegalArgumentException(ClientIllegalArgumentException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleInternalServerError(Exception e) {
//        log.error(e.getMessage());
//        return ResponseEntity.internalServerError().body("서버 관리자에게 문의하세요");
//    }
}
