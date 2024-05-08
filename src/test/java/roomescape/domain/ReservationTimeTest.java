package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> new ReservationTime(1L, LocalTime.now()))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ReservationTime(LocalTime.now()))
                .doesNotThrowAnyException();
    }
}
