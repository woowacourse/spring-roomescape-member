package roomescape.common;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.common.payload.ErrorResponse;
import roomescape.reservation.exception.PastReservationNotAllowedException;
import roomescape.reservation.exception.ReservationAccessDeniedException;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservationtime.exception.ReservationTimeDuplicatedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException e) {
        return new ErrorResponse("존재하지 않는 요청입니다.");
    }

    @ExceptionHandler(ReservationDuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleReservationAlreadyExists(ReservationDuplicatedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeDuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleReservationTimeDuplicated(ReservationTimeDuplicatedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(PastReservationNotAllowedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handlePastReservationNotAllowed(PastReservationNotAllowedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ResourceInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInUse(ResourceInUseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleReservationAccessDenied(ReservationAccessDeniedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::resolveFieldErrorMessage)
                .collect(Collectors.joining(", "));
        return new ErrorResponse(message);
    }

    private String resolveFieldErrorMessage(FieldError error) {
        if ("typeMismatch".equals(error.getCode())) {
            return error.getField() + ": 입력 형식이 잘못되었습니다.";
        }
        return error.getField() + ": " + error.getDefaultMessage();
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.info("에러 핸들링 - {}", e.getClass());
        return new ErrorResponse("%s: 입력 형식이 잘못되었습니다.".formatted(e.getName()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ErrorResponse("%s: 입력값이 필요합니다.".formatted(e.getParameterName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse("입력 형식이 잘못되었습니다.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse("API 메서드가 잘못되었습니다.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception e) {
        logger.error("서버 에러 발생({}) - {}", e.getClass(), e.getMessage());
        return new ErrorResponse("예상하지 못한 오류가 발생했습니다.");
    }
}
