package roomescape.exception.handler;

import static roomescape.exception.HttpStatusMapper.STATUS_MAP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;
import roomescape.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException) {
        HttpStatus httpStatus = STATUS_MAP.get(customException.getClass());

        if(httpStatus == null) {
            throw new RuntimeException(customException.getMessage(), customException);
        }

        return getResponse(httpStatus, customException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        return getResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException 발생: ", e);
        return getResponse(HttpStatus.BAD_REQUEST, "요청하신 처리를 완료할 수 없습니다. 데이터 제약 조건을 확인해 주세요.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException 발생: ", e);
        return getResponse(HttpStatus.BAD_REQUEST, "요청하신 JSON 데이터의 포맷이 잘못되었습니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생: ", e);
        return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 내부 오류가 발생했습니다. 관리자에게 문의하세요.");    }

    private ResponseEntity<ErrorResponse> getResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), message), httpStatus);
    }
}
