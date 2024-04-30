package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
     private final Logger logger = LoggerFactory.getLogger(getClass());

    // TODO: DateTimeParseException은 Exception보다 구체 타입인데 Exception이 먼저 잡히는 이유 찾기
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDateTimeParseException(HttpMessageNotReadableException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>("요청 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>("서버 내부에서 에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
