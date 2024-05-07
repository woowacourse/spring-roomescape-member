package roomescape.handle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.domain.exception.InvalidRequestBodyFieldException;
import roomescape.domain.exception.InvalidReservationTimeException;
import roomescape.service.exception.DeleteException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = InvalidRequestBodyFieldException.class)
    public ResponseEntity<String> handleInvalidRequestBodyFieldException(InvalidRequestBodyFieldException invalidRequestBodyFieldException) {
        logger.debug(invalidRequestBodyFieldException);
        invalidRequestBodyFieldException.printStackTrace();
        return new ResponseEntity<>(invalidRequestBodyFieldException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DeleteException.class)
    public ResponseEntity<String> handleDeleteException(DeleteException deleteException) {
        logger.debug(deleteException);
        deleteException.printStackTrace();
        return new ResponseEntity<>(deleteException.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = InvalidReservationTimeException.class)
    public ResponseEntity<String> handleInvalidReservationException(InvalidReservationTimeException invalidReservationTimeException) {
        logger.debug(invalidReservationTimeException);
        invalidReservationTimeException.printStackTrace();
        return new ResponseEntity<>(invalidReservationTimeException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        logger.error(exception);
        exception.printStackTrace();
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
