package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        Name name = new Name("호돌");
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(LocalTime.now());
        Theme theme = new Theme("name", "description", "thumbnail");

        assertThatCode(() -> new Reservation(name, date, time, theme))
                .doesNotThrowAnyException();
    }
}
