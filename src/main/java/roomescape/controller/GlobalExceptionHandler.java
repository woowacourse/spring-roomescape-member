package roomescape.controller;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Map;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors()
            .stream()
            .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public String handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife) {
            return handleInvalidFormat(ife);
        }
        return "유효하지 않은 형식의 요청입니다.";
    }

    private String handleInvalidFormat(final InvalidFormatException ife) {
        String invalidFieldNames = ife.getPath().stream().map(Reference::getFieldName)
            .collect(joining(", "));
        return String.format("유효하지 않은 형식의 요청입니다.\n필드 : %s\n요청 내용 : %s", invalidFieldNames,
            ife.getValue());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public String handleIllegalState(IllegalStateException ex) {
        return ex.getMessage();
    }
}
