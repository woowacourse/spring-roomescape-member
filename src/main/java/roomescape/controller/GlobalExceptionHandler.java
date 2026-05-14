package roomescape.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String UNEXPECTED_ERROR = "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    public static final String DATABASE_ERROR = "데이터 베이스 관련 오류가 발생했습니다. 관리자에게 문의해주세요";

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String dataAccessExceptionHandle(DataAccessException e) {
        log.error("DataAccessException 발생: {}", e.getMessage(), e);
        return DATABASE_ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.warn("IllegalArgumentException 발생: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandle(Exception e) {
        log.error("예상치 못한 예외 발생: {}", e.getMessage(), e);
        return UNEXPECTED_ERROR;
    }
}
