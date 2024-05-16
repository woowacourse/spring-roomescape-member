package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("id가 같으면 같은 hash 값을 갖고 동일한 객체이다.")
    void hash_equals_override_test() {
        Reservation reservation1 = new Reservation(
                1L,
                1L, "안돌", "admin", "email", "password",
                1L, "테마이름", "테마내용", "테마썸네일",
                "2023-09-08",
                1L, "15:30");
        Reservation reservation2 = new Reservation(
                1L,
                2L, "재즈", "admin", "email", "password",
                1L, "테마이름", "테마내용", "테마썸네일",
                "2024-04-22",
                2L, "17:30");

        assertAll(
                () -> assertThat(reservation1.hashCode()).isEqualTo(reservation2.hashCode()),
                () -> assertThat(reservation1).isEqualTo(reservation2)
        );
    }
}
