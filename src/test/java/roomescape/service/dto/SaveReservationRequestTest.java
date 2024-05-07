package roomescape.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.IllegalUserRequestException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SaveReservationRequestTest {

    @Test
    @DisplayName("이름이 정상 입력될 경우 성공한다.")
    void checkNameBlank_Success() {
        assertThatCode(() -> new SaveReservationRequest("capy", LocalDate.now(), 1L, 1L))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("이름이 빈칸인 경우 예외가 발생한다.")
    void checkNameBlank_Failure(String name) {
        assertThatThrownBy(() -> new SaveReservationRequest(name, LocalDate.now(), 1L, 1L))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("이름은 빈칸일 수 없습니다.");
    }
}
