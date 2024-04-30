package roomescape.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SaveReservationRequestTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("이름이 빈칸인 경우 예외가 발생한다.")
    void nameBlank(String name) {
        assertThatThrownBy(() -> new SaveReservationRequest(name, LocalDate.now(), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈칸일 수 없습니다.");
    }
}
