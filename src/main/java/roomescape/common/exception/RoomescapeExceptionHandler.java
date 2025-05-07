package roomescape.common.exception;

import java.util.zip.DataFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomescapeExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, DataFormatException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(IllegalStateException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleException(DuplicatedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ResourceInUseException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleException(ResourceInUseException ex) {
        return ex.getMessage();
    }
}
