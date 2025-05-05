package roomescape.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import roomescape.common.exception.CustomException;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.InvalidInputException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.ReservationDateException;
import roomescape.common.exception.ResourceInUseException;

@ControllerAdvice
public class RoomescapeExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RoomescapeExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleException(IllegalStateException ex) {
        throw new CustomException(HttpStatus.BAD_REQUEST, "현재 시스템 상태에서는 요청을 처리할 수 없습니다");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        throw new CustomException(HttpStatus.BAD_REQUEST, "유효하지 않은 값이 전달되었습니다");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다");
    }

    @ExceptionHandler(ReservationDateException.class)
    public ResponseEntity<String> handleException(ReservationDateException ex) {
        throw new CustomException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleException(InvalidInputException ex) {
        throw new CustomException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<String> handleException(DuplicatedException ex) {
        throw new CustomException(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<String> handleException(ResourceInUseException ex) {
        throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error(ex.getMessage());
        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
    }
}