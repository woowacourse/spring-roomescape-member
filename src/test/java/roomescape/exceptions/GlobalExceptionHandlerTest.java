package roomescape.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("데이터를 찾을 수 없을 경우, 404 에러가 발생한다.")
    void handleEntityNotFound_shouldReturnNotFoundStatus() {
        // given
        EntityNotFoundException exception = new EntityNotFoundException("엔티티를 찾을 수 없습니다");

        // when
        ProblemDetail result = globalExceptionHandler.handleEntityNotFound(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("잘못된 인자를 전달할 없을 경우, 400 에러가 발생한다.")
    void handleIllegalArgumentException() {
        // given
        IllegalArgumentException exception = new IllegalArgumentException("유효하지 않은 값입니다.");

        // when
        ProblemDetail result = globalExceptionHandler.handleIllegalArgument(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("데이터 무결성 오류가 발생할 경우, 400 에러가 발생한다.")
    void handleDataIntegrityViolation() {
        // given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("데이터 무결성 위반이 발생했습니다.");

        // when
        ProblemDetail result = globalExceptionHandler.handleDataIntegrityViolation(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("데이터 중복 생성 시, 400 에러가 발생한다.")
    void handleEntityDuplicate() {
        // given
        EntityDuplicateException exception = new EntityDuplicateException("중복된 데이터입니다.");

        // when
        ProblemDetail result = globalExceptionHandler.handleEntityDuplicate(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("예상하지 못한 오류 발생 시, 400 에러가 발생한다.")
    void handleJsonParseException() {
        // given
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("요청된 JSON의 형태가 잘못되었습니다.");

        // when
        ProblemDetail result = globalExceptionHandler.handleJsonParseException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("예상하지 못한 오류 발생 시, 500 에러가 발생한다.")
    void handleException() {
        // given
        Exception exception = new Exception("서버 내부 오류가 발생했습니다.");

        // when
        ProblemDetail result = globalExceptionHandler.handleException(exception);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getTitle());
        assertEquals(exception.getMessage(), result.getDetail());
        assertNotNull(result.getProperties().get("timestamp"));
    }
}
