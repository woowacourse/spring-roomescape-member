package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import roomescape.dto.request.AdminReservationCreationRequest;

@Disabled
class AdminReservationCreationRequestTest {

    @DisplayName("비어있는 멤버를 허용하지 않는다")
    @Test
    void validateFieldName() {
        assertThatThrownBy(() -> new AdminReservationCreationRequest(LocalDate.now(), 1L, 1L, null))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("[ERROR] 멤버는 빈 값을 허용하지 않습니다.");
    }

    @DisplayName("비어있는 날짜를 허용하지 않는다.")
    @Test
    void validateFieldDate() {
        assertThatThrownBy(() -> new AdminReservationCreationRequest(null, 1L, 1L, 1L))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("[ERROR] 날짜는 빈 값을 허용하지 않습니다.");
    }

    @DisplayName("비어있는 시간을 허용하지 않는다.")
    @Test
    void validateFieldTime() {
        assertThatThrownBy(() -> new AdminReservationCreationRequest(LocalDate.now(), null, 1L, 1L))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
    }

    @DisplayName("비어있는 테마를 허용하지 않는다.")
    @Test
    void validateFieldTheme() {
        assertThatThrownBy(() -> new AdminReservationCreationRequest(LocalDate.now(), 1L, null, 1L))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("[ERROR] 테마는 빈 값을 허용하지 않습니다.");
    }
}
