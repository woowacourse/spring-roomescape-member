package roomescape.common;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.common.exception.CustomException;
import roomescape.common.exception.InvalidDateException;

@ExtendWith(SoftAssertionsExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @DisplayName("IllegalArgumentException이 발생하면 400 Bad Request와 함께 메시지를 반환한다.")
    @Test
    void handle_illegal_argument_exception(SoftAssertions softly) {
        String errorMessage = "잘못된 인자가 전달되었습니다.";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @DisplayName("NullPointerException이 발생하면 400 Bad Request와 함께 메시지를 반환한다.")
    @Test
    void handle_null_pointer_exception(SoftAssertions softly) {
        String errorMessage = "널 값이 전달되었습니다.";
        NullPointerException exception = new NullPointerException(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @DisplayName("CustomException이 발생하면 400 Bad Request와 함께 [ERROR] 접두사가 붙은 메시지를 반환한다.")
    @Test
    void handle_custom_exception(SoftAssertions softly) {
        String errorMessage = "날짜를 입력해주세요";
        CustomException exception = new InvalidDateException(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleCustomException(exception);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(response.getBody()).isEqualTo("[ERROR] " + errorMessage);
    }
}
