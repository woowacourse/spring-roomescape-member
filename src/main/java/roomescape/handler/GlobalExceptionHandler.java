package roomescape.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.response.ErrorResponse;
import roomescape.exception.AssociatedReservationExistsException;
import roomescape.exception.DuplicateEntityException;
import roomescape.exception.EntityNotExistException;
import roomescape.exception.InvalidInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception exception) {
        System.out.println(exception.getMessage());
        return generateErrorResponse("예상하지 못한 예외가 발생했다. 자세한 사항은 관리자에게 문의하라.");
    }

    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidInput(InvalidInputException exception) {
        return generateErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = EntityNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotExist(EntityNotExistException exception) {
        return generateErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = AssociatedReservationExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAssociateReservationExists(AssociatedReservationExistsException exception) {
        return generateErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEntity(DuplicateEntityException exception) {
        return generateErrorResponse(exception.getMessage());
    }

    private ErrorResponse generateErrorResponse(String message) {
        return new ErrorResponse(message);
    }
}
