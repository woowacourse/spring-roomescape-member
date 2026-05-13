package roomescape.controller.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(
            MissingPathVariableException exception
    ) {
        log.warn("[Missing Path Variable]", exception);

        String message = "필수 경로 변수가 누락되었습니다: "
                + exception.getVariableName();
        ErrorResponse response = new ErrorResponse(message);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException exception
    ) {
        log.warn("[Missing Request Parameter]", exception);

        ErrorResponse response = new ErrorResponse(
                "필수 파라미터가 누락되었습니다: "
                        + exception.getParameterName()
        );

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        log.warn("[Type Mismatch]", exception);

        String message = "파라미터 타입이 일치하지 않습니다."
                + " 필드명: " + exception.getName()
                + " 입력값: " + exception.getValue()
                + " 기대 타입: " + exception.getRequiredType();
        ErrorResponse response = new ErrorResponse(message);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("[Http Message Not Readable]", e);

        String message = "HTTP 요청 본문을 읽을 수 없습니다. JSON 형식이나 데이터 타입을 확인하세요.";

        if (e.getCause() instanceof InvalidFormatException invalidFormat) {
            message += " ("
                    + "잘못된 값: " + invalidFormat.getValue()
                    + ", 기대 타입" + invalidFormat.getTargetType()
                    + ")";
        }
        ErrorResponse response = new ErrorResponse(message);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            EntityNotFoundException.class,
            InUseEntityException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception) {
        log.error("[Internal Server Error]", exception);
        ErrorResponse response = new ErrorResponse("예상하지 못한 예외가 발생했습니다.");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}
