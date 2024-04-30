package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> new Reservation(1L, "wiib", LocalDate.now(), new ReservationTime(1L, LocalTime.now())))
            .doesNotThrowAnyException();
        assertThatCode(() -> new Reservation("wiib", LocalDate.now(), new ReservationTime(1L, LocalTime.now())))
            .doesNotThrowAnyException();

    }

    @DisplayName("빈 이름으로 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void create_WithBlankName(String name) {
        assertThatThrownBy(() -> new Reservation(name, LocalDate.MAX, new ReservationTime(LocalTime.MAX)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
