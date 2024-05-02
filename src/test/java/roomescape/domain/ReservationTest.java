package roomescape.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new Reservation(1L, new Name("aa"), LocalDate.parse("9999-12-31"),
                RESERVATION_TIME_FIXTURE, ROOM_THEME_FIXTURE));
    }
}
