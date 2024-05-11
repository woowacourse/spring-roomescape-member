package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
        return createErrorResponse(HttpStatus.CONFLICT, e);
    }

    // [질문] 서버 내에 관리하지 못한 예외가 발생할 경우 사용자에게 해당 메세지를 노출을 막기 위해 만들었지만
    // 테스트 할 때 정확한 내용을 보여주지 않아 주석 처리 후 다시 확인하는 작업을 합니다.
    // 컨트롤러의 로직 검증을 할 때 RestAssured 를 사용해서 그런거 같은데 더 나은 방법이 있는지
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, Exception e) {
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(e.getMessage()));
    }
}
