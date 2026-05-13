package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception) {
        log.error("[Internal Server Error]", exception);

        ErrorResponse response = new ErrorResponse("예상하지 못한 예외가 발생했습니다.");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}
