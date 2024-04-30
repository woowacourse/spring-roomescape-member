package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    @Test
    @DisplayName("id, name, reservationDate, reservationTime 을 통해 도메인을 생성한다.")
    void create_with_id_name_reservationDate_reservationTime() {
        assertThatCode(() ->
                new Reservation(
                        null,
                        new Name("jerry"),
                        ReservationDate.from("2024-04-03"),
                        ReservationTime.from(null, "10:00")))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("id, 문자열들 을 통해 도메인을 생성한다.")
    void some2() {
        assertThatCode(() ->
                Reservation.from(
                        null,
                        "jerry",
                        "2024-04-03",
                        ReservationTime.from(null, "10:00")))
                .doesNotThrowAnyException();
    }
}
