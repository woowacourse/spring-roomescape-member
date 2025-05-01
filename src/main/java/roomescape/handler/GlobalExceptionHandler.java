package roomescape.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.InvalidInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(InvalidInputException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleSQLException(DataIntegrityViolationException ex) {
        return "예약이 존재하여 제거할 수 없다.";
    }
}
