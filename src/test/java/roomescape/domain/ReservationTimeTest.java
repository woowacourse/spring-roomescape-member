package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        LocalTime startAt = LocalTime.now();

        assertThatCode(() -> new ReservationTime(startAt))
                .doesNotThrowAnyException();
    }
}
