package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.ReservationTimeCreationRequest;

@Disabled
class ReservationTimeCreationRequestTest {

    @DisplayName("NULL인 시작시간으로 예약시간 생성 요청를 생성할 수 없습니다")
    @Test
    void canNotCreateBecause() {
        LocalTime nullStartAt = null;
        assertThatThrownBy(() -> new ReservationTimeCreationRequest(nullStartAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
    }
}
