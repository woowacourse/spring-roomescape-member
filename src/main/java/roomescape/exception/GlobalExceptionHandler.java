package roomescape.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalUserRequestException.class)
    public ResponseEntity<ProblemDetail> handleIllegalUserRequestException(IllegalUserRequestException exception) {
        logErrorMessage(exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getBody());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationException(AuthorizationException exception) {
        logErrorMessage(exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getBody());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ProblemDetail> handleForbiddenException(ForbiddenException exception) {
        logErrorMessage(exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getBody());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logErrorMessage(exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        problemDetail.setTitle("유효하지 않은 요청 데이터입니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(value = JsonMappingException.class)
    public ResponseEntity<ProblemDetail> handleJsonMappingException(JsonMappingException exception) {
        logErrorMessage(exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getPath().get(0).getFieldName() + " 필드의 값이 유효하지 않습니다.");
        problemDetail.setTitle("유효하지 않은 요청 데이터입니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        logErrorMessage(exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getParameter().getParameterName() + " URL 파라미터의 값이 유효하지 않습니다.");
        problemDetail.setTitle("유효하지 않은 요청 데이터입니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        logErrorMessage(exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getParameterName() + " URL 파라미터의 값은 비어있을 수 없습니다.");
        problemDetail.setTitle("유효하지 않은 요청 데이터입니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException exception) {
        logErrorMessage(exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 에러입니다.");
        problemDetail.setTitle("서버 에러입니다.");
        return ResponseEntity.internalServerError().body(problemDetail);
    }

    private void logErrorMessage(Exception exception) {
        exception.printStackTrace(System.err);
    }
}
