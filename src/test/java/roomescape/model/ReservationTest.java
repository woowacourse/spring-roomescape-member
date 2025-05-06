package roomescape.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "12345678912"})
    void 유효하지_않은_이름_예외처리(String name) {
        assertThatThrownBy(() -> new Reservation(null, name, null, null, null))
                .isInstanceOf(IllegalStateException.class);
    }
}
