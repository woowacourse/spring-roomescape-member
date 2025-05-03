package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.BusinessRuleViolationException;
import roomescape.common.CoreException;
import roomescape.common.NotFoundEntityException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiFailResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(new ApiFailResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiFailResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new ApiFailResponse("요청 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiFailResponse> handleCoreException(CoreException e) {
        return ResponseEntity.badRequest().body(new ApiFailResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<ApiFailResponse> handleNotFoundEntityException(NotFoundEntityException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailResponse(e.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiFailResponse> handleBusinessRuleViolationException(BusinessRuleViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiFailResponse(e.getMessage()));
    }
}
