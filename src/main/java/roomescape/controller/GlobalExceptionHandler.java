package roomescape.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String UNEXPECTED_ERROR = "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    public static final String DATABASE_ERROR = "데이터 베이스 관련 오류가 발생했습니다. 관리자에게 문의해주세요";
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String dataAccessExceptionHandle(DataAccessException e) {
        log.error("데이터베이스 관련 오류가 발생했습니다", e);
        return DATABASE_ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.error("잘못된 요청입니다.", e);
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandle(Exception e) {
        log.error("서버 내부 오류입니다.", e);
        return UNEXPECTED_ERROR;
    }

    @ExceptionHandler(BeanInstantiationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBinding(BeanInstantiationException e) {
        log.error("빈 초기화중 문제가 발생했습니다.", e);
        return e.getMessage();
    }
}
