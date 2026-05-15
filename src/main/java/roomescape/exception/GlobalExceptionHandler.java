package roomescape.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "존재하지 않는 예약입니다.",
                        "RESERVE404_001"));
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleReservationAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "이미 예약이 존재합니다.",
                        "RESERVATION409_001"));
    }

    @ExceptionHandler(PastReservationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handlePastReservation() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "지나간 날짜와 시간으로는 예약할 수 없습니다.",
                        "RESERVATION400_001"));
    }

    @ExceptionHandler(PastReservationCancelNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handlePastReservationCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "이미 지난 예약은 취소할 수 없습니다.",
                        "RESERVATION400_002"));
    }

    @ExceptionHandler(ReservationOwnerMismatchException.class)
    public ResponseEntity<ErrorResponse> handleReservationOwnerMismatch() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "예약자의 이름이 일치하지 않습니다.",
                        "RESERVATION400_003"));
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "존재하지 않는 예약시간입니다.",
                        "RESERVATION_TIME404_001"));
    }

    @ExceptionHandler(ReservationTimeInUseException.class)
    public ResponseEntity<ErrorResponse> handleReservationTimeInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "예약이 존재하는 예약시간은 삭제할 수 없습니다.",
                        "RESERVATION_TIME409_001"));
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleThemeNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "존재하지 않는 테마입니다.",
                        "THEME404_001"));
    }

    @ExceptionHandler(ThemeInUseException.class)
    public ResponseEntity<ErrorResponse> handleThemeInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "예약이 존재하는 테마는 삭제할 수 없습니다.",
                        "THEME409_001"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgument() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "유효하지 않은 요청필드입니다.",
                        "COMMON400_001"));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "경로 변수(PathVariable)가 누락됐습니다.",
                        "COMMON400_002"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "쿼리 스트링이 누락됐습니다.",
                        "COMMON400_003"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "올바른 입력값 형식이 아닙니다.",
                        "COMMON400_004"
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "올바른 쿼리 스트링 형식이 아닙니다.",
                        "COMMON400_005"
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "유효하지 않은 쿼리 스트링 값입니다.",
                        "COMMON400_006"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "예기치 못한 예외가 발생했습니다.",
                        "COMMON500_001"));
    }
}
