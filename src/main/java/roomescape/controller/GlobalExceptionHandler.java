package roomescape.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.global.ReservationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("예외 발생: {}", e.getStackTrace(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(ReservationException.class)
    public ProblemDetail handleReservationException(ReservationException e) {
        log.error("예외 발생: {}", e.getStackTrace(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
