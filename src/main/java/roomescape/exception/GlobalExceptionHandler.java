package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("읽을 수 없는 요청: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT_FORM", "요청 본문을 읽을 수 없습니다."));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Spring MVC 예외: {}", e.getMessage());

        String errorCodeName = HttpStatus.valueOf(status.value()).name();
        return ResponseEntity.status(status).body(new ErrorResponse(errorCodeName, e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("검증 실패 발생: {}", errorMessage);

        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT_VALUE", errorMessage));
    }

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomBusinessException(CustomBusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("비즈니스 로직 에러: {}", errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("예상치 못한 서버 내부 에러 발생!", ex);

        return ResponseEntity.internalServerError().body(
                new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        "서버에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해 주세요."
                )
        );
    }
}
