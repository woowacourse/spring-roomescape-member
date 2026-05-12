package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidInput(InvalidInputException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(PastReservationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePastReservation(PastReservationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReservationNotFound(ReservationNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTimeNotFound(ReservationTimeNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DuplicateReservationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateReservation(DuplicateReservationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleTimeInUse(ReservationTimeInUseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedReservationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUnauthorized(UnauthorizedReservationException e) {
        return new ErrorResponse(e.getMessage());
    }

    // compact constructor에서 던진 예외가 Jackson에 의해 감싸지는 케이스 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof InvalidInputException) {
            return new ErrorResponse(rootCause.getMessage());
        }
        return new ErrorResponse("잘못된 요청 형식입니다. 입력값을 확인해 주세요.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(Exception e) {
        return new ErrorResponse("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }
}
