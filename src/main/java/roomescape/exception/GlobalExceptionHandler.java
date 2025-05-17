package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ReservationException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationException(ReservationException exception) {
        log.warn("예약 관련 예외 발생: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = ReservationThemeException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationThemeException(ReservationThemeException exception) {
        log.warn("테마 관련 예외 발생: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = ReservationTimeException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationTimeException(ReservationTimeException exception) {
        log.warn("예약 시간 관련 예외 발생: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = MemberException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberException(MemberException exception) {
        log.warn("사용자 관련 예외 발생: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnAuthorizedException(UnAuthorizedException exception) {
        return ResponseEntity.status(401)
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(ForbiddenException exception) {
        return ResponseEntity.status(403)
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(getFieldErrorMessages(exception)));
    }

    private String getFieldErrorMessages(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((first, second) -> first + ", " + second)
                .orElse("유효성 검사 오류");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception exception) {
        log.warn("예외 발생: {}", exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto("서버 오류가 발생했습니다"));
    }
}
