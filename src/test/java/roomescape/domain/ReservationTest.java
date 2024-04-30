package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("id가 같으면 같은 hash 값을 갖고 동일한 객체이다.")
    void hash_equals_override_test() {
        Reservation reservation1 = new Reservation(1L, "재즈", "1999-11-30", 1L, "20:20");
        Reservation reservation2 = new Reservation(1L, "안돌", "2023-09-08", 2L, "20:30");

        assertAll(
                () -> assertThat(reservation1.hashCode()).isEqualTo(reservation2.hashCode()),
                () -> assertThat(reservation1).isEqualTo(reservation2)
        );
    }
}
