package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestConstants.DEFAULT_MEMBER;
import static roomescape.TestConstants.DEFAULT_THEME;
import static roomescape.TestConstants.DEFAULT_TIME;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("date가 null이면 예외를 던진다")
    void date_not_null() {
        assertThatThrownBy(() -> {
            new Reservation(
                null,
                DEFAULT_TIME,
                DEFAULT_THEME,
                DEFAULT_MEMBER
            );
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("time이 null이면 예외를 던진다")
    void time_not_null() {
        assertThatThrownBy(() -> {
            new Reservation(
                LocalDate.of(2025, 10, 5),
                null,
                DEFAULT_THEME,
                DEFAULT_MEMBER
            );
        }).isInstanceOf(NullPointerException.class);
    }
}
