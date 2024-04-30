package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("입력받은 formatter에 맞게 날짜를 String으로 변환한다.")
    void formatDate() {
        Reservation reservation = new Reservation(
                1L,
                new Name("브라운"),
                LocalDate.of(2024, 4, 24),
                null
        );

        String formatted = reservation.date(DateTimeFormatter.ISO_DATE);

        assertThat(formatted).isEqualTo("2024-04-24");
    }

    @Test
    @DisplayName("Reservation 객체의 동등성을 따질 때는 id만 확인한다.")
    void testEquals() {
        Reservation reservation1 = new Reservation(
                1L,
                new Name("브라운"),
                LocalDate.of(2024, 4, 24),
                null
        );
        Reservation reservation2 = new Reservation(
                1L,
                new Name("솔라"),
                LocalDate.of(2024, 3, 22),
                null
        );

        assertThat(reservation1).isEqualTo(reservation2);
    }
}
