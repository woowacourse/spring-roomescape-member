package roomescape.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.AssociatedReservationExistsException;
import roomescape.exception.DuplicateEntityException;
import roomescape.exception.EntityNotExistException;
import roomescape.exception.InvalidInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(RuntimeException exception) {
        return "예상하지 못한 예외가 발생했다.\n" + exception.getMessage();
    }

    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(InvalidInputException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = EntityNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEntityNotExist(EntityNotExistException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = AssociatedReservationExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAssociateReservationExists(AssociatedReservationExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateEntity(DuplicateEntityException exception) {
        return exception.getMessage();
    }
}
