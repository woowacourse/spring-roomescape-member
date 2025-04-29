package roomescape.globalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalArgument(CustomException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getStatusValue())
            .body(new ErrorResponse(
                String.valueOf(e.getStatusValue()),
                e.getMessage()
            ));
    }
}
