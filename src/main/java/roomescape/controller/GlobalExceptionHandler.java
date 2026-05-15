package roomescape.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.controller.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse dataAccessExceptionHandle(DataAccessException e) {
        log.error("DataAccessException 발생: {}", e.getMessage(), e);
        return new ErrorResponse("데이터베이스 오류가 발생했습니다. 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.warn("IllegalArgumentException 발생: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandle(Exception e) {
        log.error("예상치 못한 예외 발생: {}", e.getMessage(), e);
        return new ErrorResponse("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
}
