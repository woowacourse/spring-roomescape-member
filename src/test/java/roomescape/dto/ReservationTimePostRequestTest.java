package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.ReservationTimePostRequest;

class ReservationTimePostRequestTest {
    @Test
    @DisplayName("어떤 필드가 null일 경우 예외가 발생한다.")
    void failIfFieldIsNull() {
        assertThatThrownBy(() -> {
            new ReservationTimePostRequest(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 startAt 입력입니다.");
    }
}
