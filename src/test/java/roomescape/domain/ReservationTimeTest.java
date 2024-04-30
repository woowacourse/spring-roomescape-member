package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("id가 같으면 같은 hash 값을 갖고 동일한 객체이다.")
    void hash_equals_override_test() {
        ReservationTime time1 = new ReservationTime(1L, "11:30");
        ReservationTime time2 = new ReservationTime(1L, "12:30");

        assertAll(
                () -> assertThat(time1.hashCode()).isEqualTo(time2.hashCode()),
                () -> assertThat(time1).isEqualTo(time2)
        );
    }
}
