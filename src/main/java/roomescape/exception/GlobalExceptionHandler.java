package roomescape.exception;

import java.util.NoSuchElementException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseBody
//    public ResponseEntity<Object> handleJsonParseException(HttpMessageNotReadableException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("옳바르지 않은 JSON 형식입니다."));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("옳바르지 않은 요청입니다."));
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("필수 요청 파라미터가 누락되었습니다."));
//    }
//
//    @ExceptionHandler(TypeMismatchException.class)
//    public ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("잘못된 타입입니다."));
//    }
}
