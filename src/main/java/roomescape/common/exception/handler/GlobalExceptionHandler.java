package roomescape.common.exception.handler;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.common.exception.handler.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> handleNullPointer(final NullPointerException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                500, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(final IllegalArgumentException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleSignature(final SignatureException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                401, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(401).body(exceptionResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorize(final UnauthorizedException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                401, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(401).body(exceptionResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(final ForbiddenException exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                403, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.status(403).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException exception, final HttpServletRequest request
    ) {
        Throwable rootCause = exception.getRootCause();
        if (rootCause instanceof IllegalArgumentException) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    400, "[ERROR] " + rootCause.getMessage(), request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(exceptionResponse);
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] 요청 입력이 잘못되었습니다.", request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(final Exception exception, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] " + exception.getMessage(), request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
