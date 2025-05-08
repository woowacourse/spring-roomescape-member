package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;

class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("DB 조회 결과가 없을 때 발생하는 예외를 핸들러에서 404 응답 코드를 반환한다")
    void handleNotFoundException() {
        // given
        NotFoundException notFoundException = new NotFoundException("조회 결과가 없습니다.");
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        // when
        ResponseEntity<Void> responseEntity = globalExceptionHandler.handleNotFoundException(notFoundException);

        // then
        HttpStatusCode responseHttpStatusCode = responseEntity.getStatusCode();
        assertThat(responseHttpStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("중복될 수 없는 값을 저장할 때 발생하는 예외를 핸들러에서 409 응답 코드를 반환한다")
    void handleDuplicateException() {
        // given
        DuplicateException duplicateException = new DuplicateException("중복되는 값입니다.");
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        // when
        ResponseEntity<Void> responseEntity = globalExceptionHandler.handleDuplicateException(duplicateException);

        // then
        HttpStatusCode responseHttpStatusCode = responseEntity.getStatusCode();
        assertThat(responseHttpStatusCode).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("유효하지 않은 값을 사용할 때 발생하는 예외를 핸들러에서 400 응답 코드를 반환한다")
    void handleIllegalArgumentException() {
        // given
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("유효하지 않은 값입니다.");
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        // when
        ResponseEntity<Void> responseEntity = globalExceptionHandler.handleIllegalArgumentException(
                illegalArgumentException);

        // then
        HttpStatusCode responseHttpStatusCode = responseEntity.getStatusCode();
        assertThat(responseHttpStatusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
