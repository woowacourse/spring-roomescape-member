package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(body);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse body = new ErrorResponse(e.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        ErrorResponse body = new ErrorResponse("오류가 발생하였습니다. 관리자에게 문의해주세요");
        return ResponseEntity.internalServerError().body(body);
    }
}
