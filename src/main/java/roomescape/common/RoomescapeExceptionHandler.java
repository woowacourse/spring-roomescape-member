package roomescape.common;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.zip.DataFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.InvalidInputException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.ReservationDateException;
import roomescape.common.exception.ResourceInUseException;

@ControllerAdvice
public class RoomescapeExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleException(IllegalStateException ex) {
        return new ResponseEntity<>("현재 시스템 상태에서는 요청을 처리할 수 없습니다", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return new ResponseEntity<>("유효하지 않은 값이 전달되었습니다", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("잘못된 요청입니다", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException formatEx) {
            String targetType = formatEx.getTargetType().getSimpleName();
            if (targetType.equals("LocalDate")) {
                return new ResponseEntity<>("날짜 형식이 잘못되었습니다. yyyy-MM-dd 형식이어야 합니다.", HttpStatus.BAD_REQUEST);
            }
            if (targetType.equals("LocalTime")) {
                return new ResponseEntity<>("시간 형식이 잘못되었습니다. HH:mm 형식이어야 합니다.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("요청 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationDateException.class)
    public ResponseEntity<String> handleException(ReservationDateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleException(InvalidInputException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<String> handleException(DataFormatException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<String> handleException(DuplicatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<String> handleException(ResourceInUseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
