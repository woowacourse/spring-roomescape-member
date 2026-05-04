package roomescape.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String UNEXPECTED_ERROR = "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    public static final String DATABASE_ERROR = "데이터 베이스 관련 오류가 발생했습니다. 관리자에게 문의해주세요";

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String dataAccessExceptionHandle(DataAccessException e) {
        e.printStackTrace();
        return DATABASE_ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentExceptionHandle(IllegalArgumentException e) {
        e.printStackTrace();
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandle(Exception e) {
        e.printStackTrace();
        return UNEXPECTED_ERROR;
    }
}
